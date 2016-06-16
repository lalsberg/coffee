ALTER TABLE `order`
  ADD COLUMN club_id int(11) NOT NULL,
  ADD FOREIGN KEY fk_name(club_id) REFERENCES club(id);