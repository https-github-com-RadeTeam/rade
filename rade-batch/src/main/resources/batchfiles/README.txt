* Contexte :

	Pour r�aliser un import complet des communes dans l'application RADE
	il est n�cessaire d'utiliser deux batchs :
		- Le premier  batch "importCommuneSimpleInsee" permet l'import
		d'un fichier qui contient les communes existantes � une date donn�e.
		Cela permet d'initialiser les communes en base de donn�es RADE. 
		Pour r�aliser cet import initial on utilise le fichier de 1999 "comsimp1999.txt".
			
		- Le second batch "importCommuneInseeHistory" permet l'import d'un fichier d'historique
		qui contient l'ensemble des modifications r�alis�es sur les communes depuis 1943. 
		On utilise le fichier d'historique le plus r�cent fournit par l'INSEE.
		
	Le fichier comsimp1999.txt contient des incoh�rences avec les fichiers d'historique
	et les fichiers annuels ult�rieurs � 1999.
	Les fichiers d'historique de l'INSEE ne contiennent pas les modifications sur les DROM.

	Nous avons modifi� le fichier "comsimp1999.txt" pour corriger les probl�mes que nous avons d�tect�s
	et nous avons cr�� un nouveau fichier d'historique "mvtcommunes-97.csv" d�di� aux DROM. 

* Liste des fichiers :

*** comsimp1999_avant_PEC_2021.txt :
	Fichier INSEE des communes de 1999 partiellement corrig�. 
	55 noms de communes ont �t� modifi�s par rapport au fichier original.
	Importer avec la date du 01/01/1999.

*** comsimp1999.txt : 
	Fichier INSEE des communes de 1999 contenant les modifications
	r�alis�es sur le fichier comsimp1999_avant_PEC_2021.txt ainsi que des modications compl�mentaires.
	Dans le fichier de communes de 2021 les noms majuscules ne contiennent pas de carat�res sp�ciaux.
	Nous avons supprim� les caract�res sp�ciaux des noms en majuscules de 12795 communes.
	Nous avons �galement d�tect� et corrig� des �carts sur 4 noms en minuscule au niveaux des caract�res sp�ciaux.
	Ce fichier doit �tre import� avec la date du 01/01/1999.

*** mvtcommunes-97.csv :
	Fichier d�di� � l'historique des communes des DROM qui commencent par 97
	et qui ne sont pas trait�es dans le fichier d'historique de l'INSEE.
	Ce fichier comprent l'ajout de 19 communes (op�rations code 20) 
	et la suppression de 2 communes (op�ration code 30).
	Ce fichier doit �tre import� avec la date du 01/01/1976.