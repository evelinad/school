TemeIDP
=======

Tema 1 
IDP Arhitectura si interfata proiect

1.Mediul de dezvoltare
2.Implementare
3.Git
   
1.Mediul de dezvoltare
=======================    
    - Limbajul de programare: Java 1.7
    - Mediul de dezvoltare: Eclipse
    

2.Implementare
==============
    2.1 Arhitectura aplicatiei
           Clasa Mediator are acces la toate componentele aplicatiei si realizeaza
           legatura dintre acestea. Celelate componente(TransferManager,GUI_Main, SatteManager)
           interactioneaza prin intermediul Mediator(au acces la mediator prin intermdiul campului  med).
           Deoarece se poate face diferenta intre tipurile de operatii de transfer de fisiere
           (send & receive),am implementat patternul State care realizeaza managementul tranzitiei
           intre aceste stari.
           Clasa StateManager gestioneaza aceste stari, clasa ReceiveState corespunde unei operatii de
           download de fisier, clasa SendState corespunde unei operatii de upload de fisier.
           Tranzitiile dintre stari se fac prin intermediul metodelor setreceiveState, setSendState.
           Am utilizat patternul Command pentru gestiunea actiunilor ce se pot aplica
           asupra unui transfer in derulare(start, stop, resume, pause).
    
3.Git
======
    Pentru vesrion control, am folosit git.
    Versiunea pentru tema 1 este pe branchul tema1:
    https://github.com/evelinad/TemeIDP/tree/tema1
