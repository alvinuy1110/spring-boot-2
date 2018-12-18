-- here we don't have to include id, because the table definition indicates it will take responsibility for
-- generating it. For testing, you can include it to have predictability
insert into tbl_student (id, first_name, last_name, student_number, birth_date)
  values (1,'John','Doe','JD456','2001-06-23');
insert into tbl_student (id, first_name, last_name, student_number, birth_date)
  values (2, 'Jane','Doe','JD456',null);