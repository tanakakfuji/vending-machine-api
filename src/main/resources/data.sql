INSERT INTO vending_machine (id, name, slot_capacity, status) VALUES
(1, '健康自販機', 5, 'OPEN'),
(2, '目覚まし自販機', 6, 'CLOSED'),
(3, '甘党自販機', 4, 'OPEN');

INSERT INTO drink (id, vm_id, name, volume, price, stock) VALUES
(1, 1, 'オレンジジュース', 400, 140, 4),
(2, 1, '水', 600, 100, 10),
(3, 2, 'ブラックコーヒー', 300, 150, 5),
(4, 2, 'レッドブル', 250, 250, 3),
(5, 3, '三ツ矢サイダー', 500, 170, 4),
(6, 3, 'コカ・コーラ', 550, 170, 6);