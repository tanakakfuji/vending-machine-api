CREATE TABLE if NOT EXISTS vending_machine (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    slot_capacity INTEGER NOT NULL CHECK (slot_capacity >= 0),
    status VARCHAR(255) DEFAULT 'CLOSED' CHECK (status IN ('OPEN', 'CLOSED')) NOT NULL
);

CREATE TABLE if NOT EXISTS drink (
    id SERIAL PRIMARY KEY,
    vm_id INTEGER NOT NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    volume INTEGER NOT NULL CHECK (volume > 0),
    price INTEGER NOT NULL CHECK (price >= 0),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),

    CONSTRAINT fk_vm FOREIGN KEY (vm_id)
    REFERENCES vending_machine
(
    id
) ON DELETE CASCADE,
    CONSTRAINT unique_vm_drink_name UNIQUE
(
    vm_id,
    name
)
);