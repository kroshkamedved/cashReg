create
    definer = db_user@`192.168.88.%` procedure deleteOrder(IN orderId int(10))
DELETE FROM orders
WHERE orders.id = orderId;

