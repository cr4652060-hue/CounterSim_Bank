CREATE TABLE IF NOT EXISTS t_teller (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        teller_code VARCHAR(10) NOT NULL UNIQUE,
    teller_name VARCHAR(50) NOT NULL,
    avatar_path VARCHAR(255),
    current_cash DECIMAL(18,2) NOT NULL,
    auth_threshold DECIMAL(18,2) NOT NULL,
    overflow_threshold DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS t_teller_inventory (
                                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                  teller_code VARCHAR(10) NOT NULL,
    voucher_type VARCHAR(50) NOT NULL,
    start_no VARCHAR(30) NOT NULL,
    end_no VARCHAR(30) NOT NULL,
    total_count INT NOT NULL,
    remain_count INT NOT NULL,
    used_count INT NOT NULL,
    updated_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS t_customer (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          customer_key VARCHAR(50) NOT NULL UNIQUE,
    customer_name VARCHAR(50) NOT NULL,
    id_type VARCHAR(100),
    id_no VARCHAR(30),
    mobile VARCHAR(20),
    avatar_path VARCHAR(255),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS t_customer_media (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                customer_key VARCHAR(50) NOT NULL,
    account_category VARCHAR(100),
    medium_type VARCHAR(50),
    medium_sub_type VARCHAR(50),
    medium_no VARCHAR(30),
    customer_account_no VARCHAR(30),
    currency VARCHAR(50),
    balance_amount DECIMAL(18,2),
    open_date DATE,
    maturity_date DATE,
    term_value VARCHAR(20),
    rate_value DECIMAL(10,4),
    deposit_type VARCHAR(50),
    auto_renew_flag VARCHAR(10),
    media_status VARCHAR(20),
    voucher_no VARCHAR(30),
    remark VARCHAR(255),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS t_counter_customer (
                                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                  teller_code VARCHAR(10) NOT NULL,
    customer_key VARCHAR(50) NOT NULL,
    arrive_status VARCHAR(20) NOT NULL,
    arrive_time DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS t_sys_config (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value VARCHAR(255) NOT NULL,
    config_desc VARCHAR(255)
    );
