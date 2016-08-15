CREATE TABLE `club` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

ALTER TABLE `order`
  ADD COLUMN club_id int(11) NOT NULL,
  ADD FOREIGN KEY fk_name(club_id) REFERENCES club(id);