
drop table if exists tbl_student;

create table tbl_student(
  id BIGINT IDENTITY PRIMARY KEY,
  first_name VARCHAR(256) NOT NULL,
  last_name VARCHAR(256) NOT NULL,
  student_number VARCHAR(256) NOT NULL,
  birth_date DATE
);
