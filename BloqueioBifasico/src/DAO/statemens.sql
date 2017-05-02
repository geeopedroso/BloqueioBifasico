DROP TABLE IF EXISTS saida;
CREATE TABLE saida (
  idOperacao SERIAL,
  indiceTransacao INTEGER,
  operacao VARCHAR(10),
  itemDado VARCHAR(10),
  timestampj VARCHAR(15),
  PRIMARY KEY (idOperacao)
)
