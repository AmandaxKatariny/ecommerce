(ns ecommerce.aula2
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

; O datomic suporta somente um dos identificadores, claro, não foi imposta nenhuma
(let [calculadora {:produto/nome "Calculadora com 4 operações"}]
  (d/transact conn [calculadora]))

; Não funciona pois se você quer algo "vazio", é só não colocar
;(let [radio-relogio {:produto/nome "Radio com relogio" :produto/slug nil}]
;  (d/transact conn [radio-relogio]))

(let [celular-barato (model/novo-produto "Celular barato", "/celular-barato", 8879.00M)
      resultado @(d/transact conn [celular-barato])
      ;id-entidade (first (vals (:tempids resultado)))
      id-entidade (-> resultado :tempids vals first)
      ]
  (pprint resultado)
  (pprint @(d/transact conn [[:db/add id-entidade :produto/preco 0.1M]]))
  (pprint @(d/transact conn [[:db/retract id-entidade :produto/slug "/celular-barato"]])))

;(db/apaga-banco)
