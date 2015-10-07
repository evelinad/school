; Dumitrescu Evelina 321CA 
; Tema 3 PP


(deftemplate world
	(multislot limit) ; max size (orizontala, verticala)
	(multislot ball) ; minge
	(multislot men) ; pozitiile una dupa alta, x y -
	(slot id) ; identificator pt lume
	(multislot moves) ; lista mutarilor, initial vida
	)

; descrierea unui win-record
(deftemplate win
	(slot id) ; identificatorul lumii din care am castigat
	(multislot moves) ; lista mutarilor ce au dus la succes
)

;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt dreapta
(defrule jump_right1 
    (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<-(direction ?b1 ?b2  0 1  $?beg ?b1 ?b2 - $?end  ?id $?m )

    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 0) (+ ?b2 1)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 0) (+ ?b2 1)   0 1  $?beg  $?end ?id $?m  ))
    (retract ?d)

)

;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt dreapta
(defrule jump_right2 
    (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not (exists(world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 0)  ?men1)) 
    (test (= (+ ?b2 1)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
    (assert (direction ?men1 ?men2  0 1  $?beg ?men1 ?men2 -  $?end ?id  $?m ?b1 ?b2 - ))

)


;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt stanga
(defrule jump_left1
    (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<-(direction ?b1 ?b2  0 -1  $?beg ?b1 ?b2 - $?end  ?id $?m  )
    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 0) (+ ?b2 -1)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 0) (+ ?b2 -1)  0 -1  $?beg  $?end ?id  $?m  ))
    (retract ?d)
)


;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt stanga
(defrule jump_left2
  (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not (exists(world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 0)  ?men1)) 
    (test (= (+ ?b2 -1)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
    (assert (direction ?men1 ?men2  0 -1 $?beg ?men1 ?men2 -  $?end ?id $?m ?b1 ?b2 -   ))

)


;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt sus
(defrule jump_up1
  (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<-(direction ?b1 ?b2 -1 0 $?beg ?b1 ?b2 - $?end ?id $?m )
    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 -1) (+ ?b2 0)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 -1) (+ ?b2 0) -1 0  $?beg  $?end ?id $?m ))
    (retract ?d)
)
;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt sus
(defrule jump_up2
  (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not (exists(world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 -1)  ?men1)) 
    (test (= (+ ?b2 0)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
    (assert (direction ?men1 ?men2  -1 0  $?beg ?men1 ?men2 -  $?end ?id  $?m ?b1 ?b2 - ))

)

;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt jos
(defrule jump_down1
 (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<-(direction ?b1 ?b2  1 0  $?beg ?b1 ?b2 - $?end ?id  $?m  )
    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 1) (+ ?b2 0)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 1) (+ ?b2 0) 1 0  $?beg  $?end   ?id $?m ))
    (retract ?d)
)

;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt jos
(defrule jump_down2
 (declare (salience  2))
    ?wld<- (world	(limit ?l1  ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not(exists (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 1)  ?men1)) 
    (test (= (+ ?b2 0)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
        (assert (direction ?men1 ?men2  1 0 $?beg ?men1 ?men2 -  $?end ?id $?m ?b1 ?b2 -  ))
)
;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt dreapta sus
(defrule jump_up_right1
 (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<- (direction ?b1 ?b2 -1 1 $?beg ?b1 ?b2 - $?end ?id $?m  )
    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 -1) (+ ?b2 1)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 -1) (+ ?b2 1) -1 1  $?beg  $?end ?id $?m  ))
    (retract ?d)
)

;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt dreapta sus
(defrule jump_up_right2
 (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not(exists (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 -1)  ?men1)) 
    (test (= (+ ?b2 1)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
        (assert (direction ?men1 ?men2 -1 1 $?beg ?men1 ?men2 -  $?end  ?id $?m ?b1 ?b2 -  ))
)

;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt dreapta jos
(defrule jump_down_right1
 (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<-(direction ?b1 ?b2 1 1  $?beg ?b1 ?b2 - $?end ?id $?m  )
    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 1) (+ ?b2 1)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 1) (+ ?b2 1) 1 1 $?beg  $?end ?id $?m  ))
    (retract ?d)
)

;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt dreapta jos
(defrule jump_down_right2
(declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not (exists(world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 1)  ?men1)) 
    (test (= (+ ?b2 1)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -   $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
    (assert (direction ?men1 ?men2 1 1 $?beg ?men1 ?men2 -   $?end  ?id $?m ?b1 ?b2 -  ))
)
;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt stanga jos
(defrule jump_down_left1
 (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<-(direction ?b1 ?b2 1 -1 $?beg ?b1 ?b2 - $?end ?id $?m )
    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 1) (+ ?b2 -1)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 1) (+ ?b2 -1) 1 -1 $?beg  $?end  ?id $?m))
    (retract ?d)
)


;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt stanga jos
(defrule jump_down_left2
 (declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not(exists (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 1)  ?men1)) 
    (test (= (+ ?b2 -1)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
        (assert (direction ?men1 ?men2 1 -1 $?beg ?men1 ?men2 -  $?end  ?id $?m ?b1 ?b2 - ))
)

;; vin de pe pozitie cu men ;pot sa mut bila oriunde cu conditia de a pastra directia 
;; salt stanga jos
(defrule jump_up_left1
(declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?b1 ?b2 - $?end) (id ?id) (moves $? ?x ?y - $?))
    ?d<-(direction ?b1 ?b2 -1 -1  $?beg ?b1 ?b2 - $?end ?id $?m  )
    =>
    (assert (world	(limit ?l1 ?l2)(ball (+ ?b1 -1) (+ ?b2 -1)) (men $?beg  $?end) (id ?id) (moves $?m )))
    (assert (direction (+ ?b1 -1) (+ ?b2 -1) -1 -1 $?beg  $?end ?id $?m  ))
    (retract ?d)
)

;; vin de pozitie cu spatiu gol si incerc sa ma mut pe spatiu cu men
;; salt stanga sus
(defrule jump_up_left2 
(declare (salience  2))
    ?wld<- (world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m))
    (not (exists(world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?beg1 ?b1 ?b2 -  $?end1) (id ?id) (moves $?m))))
    (test (= (+ ?b1 -1)  ?men1)) 
    (test (= (+ ?b2 -1)  ?men2))
    =>
    (assert ( world	(limit ?l1 ?l2)(ball ?men1 ?men2) (men $?beg ?men1 ?men2 -  $?end) (id ?id) (moves $?m ?b1 ?b2 -)))
        (assert (direction ?men1 ?men2 -1 -1 $?beg ?men1 ?men2 -  $?end ?id $?m ?b1 ?b2 -  ))
)
  
;; functia testeaza daca am ajuns sau nu pe ultima linie cu bila
(defrule winwin
  (declare (salience 600))
  ?w<-(world	(limit ?l1 ?l2)(ball ?b1 ?b2) (men $?) (id ?id) (moves $?m))
  (test  (= (- ?l1 1)?b1) )
  =>

  (assert (win (id ?id) (moves $?m  ?b1  ?b2 -)))
) 


;; It's high time we clean this mess up
(defrule cleanup2
  (declare (salience -700))
  ?q2<- (world	(limit ?l11 ?l21)(ball ?b11 ?b21) (men $?) (id ?id) (moves $?m1)) 
  =>  
  (retract ?q2)
)

(defrule functie
      (declare  (salience 1000))
      (win (id ?id) (moves $?))
      ?w<-(world	(limit ? ?)(ball ? ?) (men $?) (id ?id) (moves $?))
      => 
      (retract ?w)
)

(defrule cleanup3
  (declare (salience -700))
  ?q1<- (direction $?)
  =>  
  (retract ?q1)
)




