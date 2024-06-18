CREATE TABLE MY_PRODUCT (
                            ID 		SERIAL 			NOT NULL PRIMARY KEY,
                            NAME 	TEXT 			NOT NULL,
                            PRICE	NUMERIC(5, 2) 	NOT NULL,
                            WEIGHT	TEXT			NOT NULL,
                            COLOR	TEXT			NOT NULL
)


--DROP TABLE MY_PRODUCT

INSERT INTO MY_PRODUCT VALUES (1, 'ASUS ROGY ALLY', 222.22, '200gr', 'White');
INSERT INTO MY_PRODUCT VALUES (2, 'Legion Go 512GB e 16GB', 980.44, '230gr', 'Black');
INSERT INTO MY_PRODUCT VALUES (3, 'BoboVR M3 Pro', 55.22, '110gr', 'White');
INSERT INTO MY_PRODUCT VALUES (4, 'ASUS ROGY ALLY X', 322.00, '222gr', 'White');

select * from MY_PRODUCT
where name like ('ASUS%') and color = 'White'