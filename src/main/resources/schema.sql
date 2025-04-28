CREATE TYPE change_type AS ENUM ('CREATED', 'UPDATED', 'DELETED');
CREATE TYPE backup_status AS ENUM ('IN_PROGRESS', 'COMPLETED', 'FAILED', 'SKIPPED');


CREATE TABLE file_metadata(
                              id BIGSERIAL PRIMARY KEY,
                              file_name VARCHAR(50) NOT NULL,
                              content_type VARCHAR(50) NOT NULL,
                              file_size INTEGER NOT NULL
);

CREATE TABLE departments (
                             id BIGSERIAL PRIMARY KEY,
                             department_name VARCHAR(100) NOT NULL UNIQUE,
                             department_description TEXT NOT NULL,
                             department_established_date DATE NOT NULL
);


CREATE TABLE employees (
                           id BIGSERIAL PRIMARY KEY,
                           employee_number VARCHAR(20) UNIQUE NOT NULL,
                           employee_name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) UNIQUE NOT NULL,
                           department_id INTEGER NOT NULL,
                           position VARCHAR(20) NOT NULL,
                           hire_date DATE NOT NULL,
                           employee_state VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                           profile_image_id INTEGER,

                           CONSTRAINT fk_department
                               FOREIGN KEY (department_id)
                                   REFERENCES departments(id),

                           CONSTRAINT fk_profile_image
                               FOREIGN KEY (profile_image_id)
                                   REFERENCES file_metadata(id)
);




CREATE TABLE change_logs (
                             id BIGSERIAL PRIMARY KEY,
                             employee_id BIGINT,
                             type change_type NOT NULL,
                             memo VARCHAR(255),
                             ip_address VARCHAR(20) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

                             CONSTRAINT fk_change_logs
                                 FOREIGN KEY (employee_id)
                                     REFERENCES employees(id)
                                     ON DELETE SET NULL
);

CREATE TABLE change_log_details (
                                    id BIGSERIAL PRIMARY KEY,
                                    change_log_id BIGINT NOT NULL,
                                    property_name VARCHAR(50) NOT NULL,
                                    before_value VARCHAR(255),
                                    after_value VARCHAR(255),

                                    CONSTRAINT fk_change_log_details
                                        FOREIGN KEY (change_log_id)
                                            REFERENCES change_logs(id)
                                            ON DELETE CASCADE
);


CREATE TABLE backup_histories(
                                 id BIGSERIAL PRIMARY KEY,
                                 operator VARCHAR(25) NOT NULL,
                                 started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 status backup_status NOT NULL,
                                 backup_file_id BIGINT,

                                 CONSTRAINT backup_file_fk
                                     FOREIGN KEY (backup_file_id)
                                         REFERENCES file_metadata(id)
                                         ON DELETE SET NULL
);

CREATE TABLE employee_stats (
                                id BIGSERIAL PRIMARY KEY,
                                employee_count BIGINT NOT NULL,
                                joined_employee_count BIGINT NOT NULL,
                                left_employee_count BIGINT NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                stat_date DATE NOT NULL UNIQUE

);

CREATE TABLE department_stats (
                                  id BIGSERIAL PRIMARY KEY,
                                  department_name VARCHAR(100) NOT NULL,
                                  employee_count BIGINT NOT NULL,
                                  joined_employee_count BIGINT NOT NULL,
                                  left_employee_count BIGINT NOT NULL,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  stat_date DATE NOT NULL,
                                  employee_state VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                                  CONSTRAINT uq_department_state_date
                                      UNIQUE (stat_date, employee_state, department_name)
);

CREATE TABLE position_stats (
                                id BIGSERIAL PRIMARY KEY,
                                position_name VARCHAR(20) NOT NULL,
                                employee_count BIGINT NOT NULL,
                                joined_employee_count BIGINT NOT NULL,
                                left_employee_count BIGINT NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                stat_date DATE NOT NULL,
                                employee_state VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                                CONSTRAINT uq_position_state_date
                                    UNIQUE (stat_date, employee_state, position_name)
);

CREATE TABLE shedlock (
                          name VARCHAR(64) PRIMARY KEY,
                          lock_until TIMESTAMP(3) NULL,
                          locked_at TIMESTAMP(3) NULL,
                          locked_by VARCHAR(255) NOT NULL
);

create index idx_employee_stats_stat_date
    on employee_stats(stat_date ASC);

ALTER TABLE departments ALTER COLUMN department_name TYPE VARCHAR(100) COLLATE "ko-x-icu";