		TEMA 2 APD

			Volintiru Mihai Catalin 336CA


	Pentru inceput, am preluat constantele (numele fisierelor de input si de
output, argumentele) si le-am pus in variabile pentru a fi mai usor de accesat.
Modelul principal pe care m-am bazat pentru a rezolva tema este modelul 
Replicated Workers.
	O sa impart in doua thredurile despre care o sa vorbesc: threadurile order
si threadurile product.
	Threadurile order trebuie sa citeasca toate in paralel din acelasi fisier,
asa ca am ales sa fac citirea cu BufferedReader, care, din cate am citit, este
thread safe. Fiecare thread order are date necesare nu numai pentru el, ci si
pentru threadurile product, pe care trebuie sa le paseze mai departe. Avand in
vedere patternul folosit, threadurile order preiau stringul citit si il adauga
in "sacul cu taskuri", de unde o sa fie preluat de threadurile product. In
final, daca totul este ok si produsele sunt livrate, acestea vor scrie in 
fisierul de output daca comanda a fost shipped sau nu (aici am pus si lock si 
synchronized ca sa nu existe vreo problema de race condition intre threaduri,
avand in vedere ca printez stringul si new-line ul in 2 linii de cod separate).
	Threadurile product sunt practic "work-erii" intre care se impart taskurile
provenite de la threadurile order. Acestea prelucreaza stringul primit si cauta
in fisierul de produse toate produsele corespunzatoare comenzii pe care acestia
au primit-o. Astfel, fiecare thread product parcure singur de la cap la coada
fisierul cu produse (nu neaparat de la cap la coada, pentru ca daca a gasit
numarul de produse pe care il avea ca target, se opreste) si da shipped la 
produsele potrivite. Citirea din fisier o fac cu un simplu scanner, nu am con-
siderat ca ar trebui sa fac treaba asta in paralel, pentru ca oricum eram limi-
tat la numarul de threaduri de nivel 2, asa ca am decis ca fiecare thread
product sa isi faca propriul obiect Scanner si sa citeasca singur. Threadurile
product implementeaza interfata Callable, deoarece are avantajul ca poate sa
arunce o exceptie si sa intoarca un rezultat(in cazul meu, un string). Acelasi
lucru ca si la threadurile order, am pus un lock si synchronized la scrierea in
fisier pentru ca printarea stringului si a new-line-ului le fac in 2 linii de
de cod separate si poate duce la race-condition.
