module Robot
where

import Types


type Pair = (Int, Int)

-- definire element de memorie
data RobotMemory 
	= Rm
		{ positions :: [Pair]
		 ,values :: [Pair]
		 ,lastPoz :: Pair	
		}		

-- startRobot
startRobot s = (Rm [(0,0)] [(0,0)] (0,0))

-- noile coordonate in functie de directie 
newMovement point direction = case direction of 
	S -> ((fst point)+1, (snd point))
	E -> ((fst point), (snd point)+1)
	N -> ((fst point)-1, (snd point))
	W -> ((fst point), (snd point)-1)


-- stabileste noua directie
getDirection poz1 poz2 
	| (((fst poz1)-(fst poz2)  == 1 ) && ((snd poz1)-(snd poz2) == 0)) = Just S
	| (((fst poz1)-(fst poz2)  == -1 ) && ((snd poz1)-(snd poz2) == 0)) = Just N
	| (((fst poz1)-(fst poz2)  == 0 ) && ((snd poz1)-(snd poz2) == 1)) = Just E
	| (((fst poz1)-(fst poz2)  == 0 ) && ((snd poz1)-(snd poz2) == -1)) = Just W
	

-- verfica daca un element se gaseste sau nu intr-o lista
contains a l = 
	if l == [] 
	then False
	else if a == (head l) 
		then True
		else contains a (tail l)	
				
-- elimina elementele din lista l1 care se regasesc in l2
removeEl l1 l2 = 
		if l1 == []
		then l1
		else if contains (head l1) l2
			then removeEl (tail l1)	l2
			else (head l1) : (removeEl (tail l1) l2)	 
	
-- returneaza lista cu toate miscarile posibile
getAllMoves poz = (map (newMovement poz) [N,S,E,W])

-- returneaza lista cu toate miscarile corecte
getRightMoves poz cs = ( getAllMoves poz) `removeEl` (map (newMovement poz) cs)


-- alege mutarea optima
chooseBestMove move1 move2  freq1 freq2 sensor1 sensor2
		| (freq1 < freq2)=  True
		| (freq1 == freq2) && (sensor1 > sensor2) = True
		| otherwise = False 


-- actualizeaza detaliile unei mutari (frecventa,senzor)
modifyDetails sensor poz [] _ = [(0, sensor)]
modifyDetails sensor poz lpoz lDet = 
	if (poz == (head lpoz))
	then ((fst (head lDet )+ 1),sensor) : (tail lDet) 
	else (head lDet):(modifyDetails sensor poz (tail lpoz) (tail lDet))	


-- returneaza detaliile unei mutari (frecventa,sensor)
gimmeDetails poz [] [] = (0,0)
gimmeDetails poz lpoz lDet =  
	if (poz == (head lpoz))
	then((fst(head lDet)),(snd(head lDet)))
	else gimmeDetails poz (tail lpoz)  (tail lDet)

-- returneaza lista de detalii pentro o lista de pozitii
makeList pozCrt lPoz lDet = 
		if pozCrt == [] 
		then []
		else 
		(gimmeDetails (head pozCrt) lPoz lDet ):makeList (tail pozCrt) lPoz lDet

-- alege mutarea optima dintr-o lista de mutari
choose poz freq sens l lDet = 
	if l == []
	then poz
	else 
	if chooseBestMove poz(head l)freq (fst (head lDet)) sens (snd(head lDet))
	then choose poz freq sens (tail l) (tail lDet)
	else 
		choose (head l)(fst(head lDet))(snd (head lDet))(tail l)(tail lDet)


-- perceiveAndAct
perceiveAndAct s cs (Rm poz values lastPoz) =  do
	let oldPoz = lastPoz  
	let lPoz = getRightMoves oldPoz cs
	let lVal = makeList lPoz poz values
	let newPoz = 
		choose (head lPoz)(fst (head lVal))(snd (head lVal))(tail lPoz)(tail lVal)
	let newValues = 
		if elem newPoz poz 
		then modifyDetails s newPoz poz values
		else (1,s):values
	let newPozList = 
		if elem newPoz poz 
		then poz 
		else newPoz : poz	
	let newDir = (getDirection newPoz oldPoz )
	( newDir,(Rm newPozList newValues newPoz))

