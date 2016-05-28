CREATE TABLE `user_order_coffee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_order_id` int(11) NOT NULL,
  `coffee_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_order_id` (`user_order_id`),
  KEY `coffee_id` (`coffee_id`),
  CONSTRAINT `user_order_coffee_ibfk_1` FOREIGN KEY (`user_order_id`) REFERENCES `user_order` (`id`),
  CONSTRAINT `user_order_coffee_ibfk_2` FOREIGN KEY (`coffee_id`) REFERENCES `coffee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;