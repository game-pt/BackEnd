-- Start Data
INSERT INTO `stat`(`code`, `name`)
VALUES('STT-001', '체력');
INSERT INTO `stat`(`code`, `name`)
VALUES('STT-002', '힘');
INSERT INTO `stat`(`code`, `name`)
VALUES('STT-003', '민첩');
INSERT INTO `stat`(`code`, `name`)
VALUES('STT-004', '지능');
INSERT INTO `stat`(`code`, `name`)
VALUES('STT-005', '운');

-- Dummy Data
INSERT INTO `story`(`code`, `name`, `desc`)
    VALUES ('STR-001', '신들의 마지막 전투',
    '오래 전, 신들이 인간 세계를 창조하고 지배했다. 그러나 신들의 세계는 시간이 흘러도 굳어져만 갔고, 신들은 자신들의 힘이 약해진 것을 깨닫게 되었다. 그래서 신들은 자신들의 마지막 힘을 모아 세계의 운명을 결정짓기로 했다. 그러던 중, 세 종족의 용사 중 한 명이 나타났다.');

INSERT INTO `race`(`code`, `name`, `story_code`)
VALUES ('RAC-001', '드래곤', 'STR-001');
INSERT INTO `race_stat`(`code`, `stat_value`, `race_code`, `stat_code`)
    VALUES ('RAC-STT-001', 10, 'RAC-001', 'STT-001'),
    ('RAC-STT-002', 6, 'RAC-001', 'STT-002'),
    ('RAC-STT-003', 6, 'RAC-001', 'STT-003'),
    ('RAC-STT-004', 2, 'RAC-001', 'STT-004'),
    ('RAC-STT-005', 8, 'RAC-001', 'STT-005');

INSERT INTO `race`(`code`, `name`, `story_code`)
VALUES ('RAC-002', '엘프', 'STR-001');
INSERT INTO `race_stat`(`code`, `stat_value`, `race_code`, `stat_code`)
    VALUES ('RAC-STT-006', 10, 'RAC-002', 'STT-001'),
    ('RAC-STT-007', 2, 'RAC-002', 'STT-002'),
    ('RAC-STT-008', 8, 'RAC-002', 'STT-003'),
    ('RAC-STT-009', 6, 'RAC-002', 'STT-004'),
    ('RAC-STT-010', 4, 'RAC-002', 'STT-005');

INSERT INTO `race`(`code`, `name`, `story_code`)
VALUES ('RAC-003', '오크', 'STR-001');

INSERT INTO `race_stat`(`code`, `stat_value`, `race_code`, `stat_code`)
    VALUES ('RAC-STT-011', 10, 'RAC-003', 'STT-001'),
    ('RAC-STT-012', 9, 'RAC-003', 'STT-002'),
    ('RAC-STT-013', 2, 'RAC-003', 'STT-003'),
    ('RAC-STT-014', 2, 'RAC-003', 'STT-004'),
    ('RAC-STT-015', 7, 'RAC-003', 'STT-005');