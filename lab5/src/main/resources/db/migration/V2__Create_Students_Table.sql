CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    mark DOUBLE,
    gender BOOLEAN
);

INSERT INTO students (id, name, mark, gender) VALUES 
('SV01', 'Nguyen Van A', 8.5, true),
('SV02', 'Tran Thi B', 7.0, false),
('SV03', 'Le Van C', 9.0, true)
ON DUPLICATE KEY UPDATE name=name;
