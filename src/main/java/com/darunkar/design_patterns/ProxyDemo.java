package com.darunkar.design_patterns;


interface ProductService{
    void getProductDetails(String productId);
}

class ProductServiceImpl implements ProductService{

    @Override
    public void getProductDetails(String productId) {
//        db call to get product details
        System.out.println("Products details for productId : " + productId);
    }
}

class ProductServiceProxy implements ProductService{

    private final ProductService productService;
    private final String userRole;

    public ProductServiceProxy(ProductService productService, String userRole) {
        this.productService = productService;
        this.userRole = userRole;
    }

    @Override
    public void getProductDetails(String productId) {

        if("ADMIN".equalsIgnoreCase(userRole)){
            System.out.println("Access Granted");
            productService.getProductDetails(productId);
        }else{
            System.out.println("Access Denied");
        }
    }
}
public class ProxyDemo {

    public static void main(String[] args) {

        ProductServiceProxy adminProxy = new ProductServiceProxy(new ProductServiceImpl(), "ADMIN");
        ProductServiceProxy userProxy = new ProductServiceProxy(new ProductServiceImpl(), "USER");

        adminProxy.getProductDetails("abc");
        userProxy.getProductDetails("abc");
    }
}
