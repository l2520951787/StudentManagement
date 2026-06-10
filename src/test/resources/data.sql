INSERT INTO students (name, ruby, nickname, mail_address, area, age, gender, remark, is_deleted)
VALUES ('赤坂浩二', 'アカサカコウジ', 'コウジ', 'akasaka@example.com', '兵庫', 35, '男', '', false),
       ('石上穂香', 'イシガミホノカ', 'ほのか', 'ishigami@example.com', '愛媛', 22, '女', '', false),
       ('卯ノ花環', 'ウノハナタマキ', 'たま', 'unohana@example.com', '愛知', 27, '女', '', false),
       ('江口博士', 'エグチヒロヒト', 'ハカセ', 'eguchi@example.com', '北海道', 30, '男', '', false),
       ('荻野祐樹', 'オギノユウキ', 'ユウ', 'ogino@example.com', '東京', 21, 'その他', '', false);

INSERT INTO students_courses (student_id, course, start_date, end_date)
VALUES (1, 'Javaコース','2025-10-27', '2026-10-27'),
       (2, 'Photoshopコース', '2025-11-15', '2026-11-15'),
       (3, 'WEBデザインコース', '2026-01-01', '2027-01-01'),
       (4, 'Excelコース', '2025-09-30', '2026-09-30'),
       (5, 'CADコース', '2026-01-31', '2027-01-31');

INSERT INTO course_status (course_id, status)
VALUES (1, '受講終了'),
       (2, '受講終了'),
       (3, '仮申込'),
       (4, '受講中'),
       (5, '本申込');
