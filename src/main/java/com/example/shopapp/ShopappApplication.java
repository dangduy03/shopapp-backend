package com.example.shopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.shopapp.repositorys")
@ComponentScan(basePackages = {
		"com.example.shopapp"
})
public class ShopappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopappApplication.class, args);
	}

}
/*
1.Download file kafka-deployment.yaml trên Google Driver, copy vào "thư mục dự án"
2.cd "thư mục dự án"
Trong bài này của mình là(trong máy tính của các bạn có thể là thư mục khác):
MacOS
cd /Users/hoangnd/Documents/code/udemy/ShopApp/
Windows:
cd C:\\code\\udemy\\ShopApp
docker rm -f zookeeper-01 zookeeper-02 zookeeper-03 kafka-broker-01
docker-compose -f ./kafka-deployment.yaml up -d zookeeper-01
docker-compose -f ./kafka-deployment.yaml up -d zookeeper-02
docker-compose -f ./kafka-deployment.yaml up -d zookeeper-03

3.Đợi khoảng 10 giây sau đó chạy lệnh này
docker-compose -f ./kafka-deployment.yaml up -d kafka-broker-01

*/