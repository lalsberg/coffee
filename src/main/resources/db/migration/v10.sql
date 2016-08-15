RENAME TABLE club TO company;

ALTER TABLE `user`
  ADD COLUMN company_id int(11) NOT NULL,
  ADD FOREIGN KEY fk_name(company_id) REFERENCES company(id);

ALTER TABLE `company` DROP FOREIGN KEY `company_ibfk_1`, DROP COLUMN `owner_id`;

ALTER TABLE `order`
	DROP FOREIGN KEY `order_ibfk_1`, 
	DROP COLUMN `club_id`,
	ADD COLUMN `company_id` int(11) NOT NULL,
	ADD FOREIGN KEY fk_name(company_id) REFERENCES company(id);