-- Booking service
CREATE USER IF NOT EXISTS 'bookinguser'@'%' IDENTIFIED BY 'bookingpass';
GRANT ALL PRIVILEGES ON bookingdb.* TO 'bookinguser'@'%';

-- Car pricing service
CREATE USER IF NOT EXISTS 'pricinguser'@'%' IDENTIFIED BY 'pricingpass';
GRANT ALL PRIVILEGES ON car_pricing_db.* TO 'pricinguser'@'%';

-- Driving license service
CREATE USER IF NOT EXISTS 'licenseuser'@'%' IDENTIFIED BY 'licensepass';
GRANT ALL PRIVILEGES ON driving_license_db.* TO 'licenseuser'@'%';

FLUSH PRIVILEGES;
