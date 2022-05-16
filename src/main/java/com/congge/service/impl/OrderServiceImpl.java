package com.congge.service.impl;

import com.congge.common.ResponseResult;
import com.congge.common.exception.BusinessException;
import com.congge.common.utils.RedisLock;
import com.congge.common.utils.ZkLock;
import com.congge.entity.Order;
import com.congge.entity.OrderItem;
import com.congge.entity.Product;
import com.congge.mapper.OrderItemMapper;
import com.congge.mapper.OrderMapper;
import com.congge.mapper.ProductMapper;
import com.congge.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    private String productId = "0001";

    private int purchaseProductNum = 1;

    @Override
    public ResponseResult getOrderById(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        return ResponseResult.success(order, 200);
    }

    /*@Override
    //@Transactional(rollbackFor = Exception.class)
    public synchronized ResponseResult createOrder() {
        log.info("准备创建订单...");
        //手动开启事务
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        //Product product = productMapper.selectById(productId);
        Product product = productMapper.selectProductById(productId);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (product == null) {
            throw new BusinessException("购买的商品不存在");
        }
        Integer currCount = product.getCount();
        if (purchaseProductNum > currCount) {
            platformTransactionManager.rollback(transaction);
            log.info("购买的商品库存数量不够了，购买的数量是:{},实际库存数是:{}", purchaseProductNum, product.getCount());
            throw new BusinessException("购买的商品库存数量不够了");
        }
        Integer leftCount = currCount - purchaseProductNum;
        product.setCount(leftCount);
        //更新商品的库存
        productMapper.updateById(product);
        //订单表和订单详情表各自插入一条数据
        String orderId = insertOrder(product);
        insertOrderItem(product, orderId);
        platformTransactionManager.commit(transaction);
        return ResponseResult.success(200, "订单创建成功");
    }*/

    private String insertOrder(Product product) {
        Order order = new Order();
        String orderId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        order.setId(orderId);
        order.setOrderAmount(product.getPrice() * purchaseProductNum);
        order.setOrderStatus(1);
        order.setReceiverName("zhangsan");
        order.setReceiverPhone("13323412345");
        orderMapper.insert(order);
        return orderId;
    }

    private void insertOrderItem(Product product, String orderId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setProductId(productId);
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItemMapper.insert(orderItem);
    }

    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized ResponseResult createOrder() {
        log.info("准备创建订单...");
        Product product = productMapper.selectProductById(productId);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(product==null){
            throw new BusinessException("购买的商品不存在");
        }
        Integer currCount = product.getCount();
        if(purchaseProductNum > currCount){
            log.info("购买的商品库存数量不够了，购买的数量是:{},实际库存数是:{}",purchaseProductNum,product.getCount());
            throw new BusinessException("购买的商品库存数量不够了");
        }
        Integer leftCount = currCount-purchaseProductNum;
        product.setCount(leftCount);
        //更新商品的库存
        productMapper.updateById(product);
        //订单表和订单详情表各自插入一条数据
        String orderId = insertOrder(product);
        insertOrderItem(product, orderId);
        return ResponseResult.success(200,"订单创建成功");
    }*/

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis锁实现
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createOrder() {
        String key = "redisKey";
        RedisLock redisLock = new RedisLock(redisTemplate,key,30);
        if(redisLock.getLock()){
            String name = Thread.currentThread().getName();
            log.info("线程:{} ->获取到了锁，准备创建订单...",name);
            Product product = productMapper.selectProductById(productId);
            if(product==null){
                throw new BusinessException("购买的商品不存在");
            }
            Integer currCount = product.getCount();
            if(purchaseProductNum > currCount){
                log.info("购买的商品库存数量不够了，购买的数量是:{},实际库存数是:{}",purchaseProductNum,product.getCount());
                throw new BusinessException("购买的商品库存数量不够了");
            }
            Integer leftCount = currCount-purchaseProductNum;
            product.setCount(leftCount);
            //更新商品的库存
            productMapper.updateByPrimaryKey(product);
            //订单表和订单详情表各自插入一条数据

            String orderId = insertOrder(product);
            //insertOrderItem(product, orderId);
            boolean unLock = redisLock.unLock();
            log.info("线程:{} ->释放锁,结果:{}",name,unLock);
        };
        return ResponseResult.success(200,"订单创建成功");
    }

    @Autowired
    private RedissonClient redissonClient;

    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createOrder() throws InterruptedException {
        String key = "redisKey";
        RLock lock = redissonClient.getLock(key);
        lock.lock();
        String name = Thread.currentThread().getName();
        try{
            Thread.sleep(15000);
            log.info("线程:{} ->获取到了锁，准备创建订单...", name);
            Product product = productMapper.selectProductById(productId);
            if (product == null) {
                throw new BusinessException("购买的商品不存在");
            }
            Integer currCount = product.getCount();
            if (purchaseProductNum > currCount) {
                log.info("购买的商品库存数量不够了，购买的数量是:{},实际库存数是:{}", purchaseProductNum, product.getCount());
                throw new BusinessException("购买的商品库存数量不够了");
            }
            Integer leftCount = currCount - purchaseProductNum;
            product.setCount(leftCount);
            //更新商品的库存
            productMapper.updateById(product);
            //订单表和订单详情表各自插入一条数据
            String orderId = insertOrder(product);
            insertOrderItem(product, orderId);
        }finally {
            lock.unlock();
            log.info("线程:{} ->释放锁", name);
        }
        return ResponseResult.success(200, "订单创建成功");
    }*/

    /**
     * zkLock分布式锁
     * @return
     * @throws InterruptedException
     */
    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createOrder() throws InterruptedException {
        log.info("进入方法");
        ZkLock zkLock = new ZkLock();
        boolean info = zkLock.getLock("order");
        String name = Thread.currentThread().getName();
        try {
            if (info) {
                Thread.sleep(15000);
                log.info("线程:{} ->获取到了锁，准备创建订单...", name);
                Product product = productMapper.selectProductById(productId);
                if (product == null) {
                    throw new BusinessException("购买的商品不存在");
                }
                Integer currCount = product.getCount();
                if (purchaseProductNum > currCount) {
                    log.info("购买的商品库存数量不够了，购买的数量是:{},实际库存数是:{}", purchaseProductNum, product.getCount());
                    throw new BusinessException("购买的商品库存数量不够了");
                }
                Integer leftCount = currCount - purchaseProductNum;
                product.setCount(leftCount);
                //更新商品的库存
                productMapper.updateByPrimaryKey(product);
                //订单表和订单详情表各自插入一条数据
                String orderId = insertOrder(product);
                insertOrderItem(product, orderId);
            }
        } finally {
            zkLock.close();
            log.info("线程:{} ->释放锁", name);
        }
        return ResponseResult.success(200, "订单创建成功");
    }*/


}
