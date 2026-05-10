INSERT INTO students (name, ruby, nickname, mail_address, area, age, gender)
VALUES ('赤坂浩二', 'アカサカコウジ', 'コウジ', 'koujiakasaka@gmail.com', '兵庫', 35, '男'),
       ('石上穂香', 'イシガミホノカ', 'ほのか', 'honokaishigami@gmail.com', '愛媛', 22, '女'),
       ('卯ノ花環', 'ウノハナタマキ', 'たま', 'tamakiunohana@gmail.com', '愛知', 27, '女'),
       ('江口博仁', 'エグチヒロヒト', 'ハカセ', 'hirohitoeguchi@gmail.com', '北海道', 30, '男'),
       ('荻野祐樹', 'オギノユウキ', 'ユウ', 'yuukiogino@gmail.com', '東京', 21, 'その他');

INSERT INTO students_courses (student_id, course, start_date, end_date)
VALUES (1, 'Javaコース', '2025-10-27', '2026-10-27'),
       (2, 'Photoshopコース', '2025-11-15', '2026-11-15'),
       (3, 'WEBデザインコース', '2026-1-16', '2027-1-16'),
       (4, 'Excelコース', '2025-9-30', '2026-9-30'),
       (5, 'CADコース', '2026-1-20', '2027-1-20');