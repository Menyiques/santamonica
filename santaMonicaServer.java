package santaMonica;
import java.util.Scanner;
import java.util.Random;


public class santaMonicaServer {
	public static Scanner in;
	
	public static int[] mazo= new int[41];//van de 0 a 39 y la 40 es el número de cartas en el mazo
	public static String estado="Esperando";	
	public static int nJugadores=0;
	public static Jugador[] jugador= new Jugador[10];
	public static String[] nombre= {"Cano","Javi","Cris","Sergio","Chito"};
	public static int[] puntosPartida= new int [10];
	public static int jugadorMano=0;
	
	public static void partida(int nj) {
		nJugadores=nj;
		
		Random ran = new Random(); 
		jugadorMano=ran.nextInt(nJugadores);
		System.out.println(nombre[jugadorMano]+" es mano elegido al azar");
		
		if (nJugadores==4) {
			baza(1);baza(2);baza(3);baza(4);baza(5);baza(6);baza(7);baza(8);baza(9);
			baza(10);baza(10);baza(10);baza(10);
			baza(9);baza(8);baza(7);baza(6);baza(5);baza(4);baza(3);baza(2);baza(1);
		}
		if (nJugadores==5) {
			baza(1);baza(8);baza(3);baza(4);baza(5);baza(6);baza(7);
			baza(8);baza(8);baza(8);baza(8);baza(8);
			baza(7);baza(6);baza(5);baza(4);baza(3);baza(2);baza(1);
		}
		if (nJugadores==6) {
			baza(1);baza(2);baza(3);baza(4);baza(5);
			baza(6);baza(6);baza(6);baza(6);baza(6);baza(6);
			baza(5);baza(4);baza(3);baza(2);baza(1);
		}
		if (nJugadores==7) {
			baza(1);baza(2);baza(3);baza(4);
			baza(5);baza(5);baza(5);baza(5);baza(5);baza(5);baza(5);
			baza(4);baza(3);baza(2);baza(1);
		}
	}

	public static void baza(int nCartasBaza) {
		
		int[][] carta= new int[5][13];
		int[][] puntos= new int[5][2];//0=Pedidas, 1=Hechas
		baraja(mazo);  
		//Reparte cartas
		for (int n=0;n<nJugadores;n++){
			for (int c=1;c<=nCartasBaza;c++) {
				carta[n][c]=dameCarta();
				carta[n][0]++;
			}
		}
		System.out.println("------------------------------------------------------------");
		System.out.println("Ronda de:"+nCartasBaza);
		System.out.println("Cartas repartidas");
		
		int pinte=99;
		int paloPinte=-1;
		
		for (int j=0;j<nJugadores;j++) {
			int jugador=j+jugadorMano;
			if (jugador>=nJugadores) {jugador-=nJugadores;}
			
			System.out.print(nombre[jugador]+" tiene ");for (int c=0;c<carta[j][0];c++) {System.out.print(translate(carta[jugador][c+1])+"("+c+"),");}System.out.println();
			
			if (pinte==99) {
				pinte=dameCarta(); 
				if (pinte==-1) {
					//No hay cartas para elegir el palo
					do {
						
					System.out.println("Elige el Pinte (0=Oros, 1=Copas, 2=Espadas, 3=Bastos :");
					pinte=Integer.parseInt(inkey()[0])*10;
					} while  (pinte<0||pinte>30);
				}
				paloPinte=pinte/10;
				System.out.println("Pinta :"+translate(pinte));
			}
			
			
			System.out.println(" Pide:");
			puntos[jugador][0]=Integer.parseInt(inkey()[0]);
			puntos[jugador][1]=0;
		}
		
		for (int baza=0;baza<nCartasBaza;baza++) {
			
			System.out.println("Baza :"+baza);
			int cartaGanadora=-1;
			int paloBaza=-1;
			int ganador=-1;
			
			for (int j=0;j<nJugadores;j++) {
				System.out.println("------------------------------------------------------------");
				int posCartaTirada=-1;
				int cartaTirada=-1;

				int jugador=j+jugadorMano;
				if (jugador>=nJugadores) {jugador-=nJugadores;}
				
				System.out.print(nombre[jugador]+" tiene ");	
				for (int c=0;c<carta[jugador][0];c++) {System.out.print(translate(carta[jugador][c+1])+"("+c+"),");}
				boolean ok=true;
				do {
				posCartaTirada=Integer.parseInt(inkey()[0]);
				cartaTirada=carta[jugador][posCartaTirada+1];
				ok=comprueba(cartaTirada,cartaGanadora,paloBaza,paloPinte,carta[jugador]);
				} while (!ok);
				System.out.println(nombre[jugador]+" tira "+translate(cartaTirada));	
				
				
				if (paloBaza==-1) {paloBaza=cartaTirada/10;}
				if (cartaGanadora == -1) {cartaGanadora=cartaTirada;}
				cartaGanadora=compara(cartaGanadora,cartaTirada,paloPinte,paloBaza);
				if (cartaGanadora==cartaTirada) {ganador=jugador;}
				System.out.println("Va ganando "+nombre[ganador]+" con "+translate(cartaGanadora));
				carta[jugador][posCartaTirada+1]=carta[jugador][carta[jugador][0]];
				carta[jugador][0]--;
		
		}
			System.out.println("------------------------------------------------------------");
			System.out.println("Gana "+nombre[ganador]+ " con "+translate(cartaGanadora));
			puntos[ganador][1]++;
			jugadorMano=ganador;
		}
		
		for (int j=0;j<nJugadores;j++) {
			int p=0;
			if (puntos[j][0]==puntos[j][1]) {
					p=p+puntos[j][0]*3+10;
				} else {
					p=p+Math.abs(puntos[j][0]-puntos[j][1])*-3-10;
				}
			puntosPartida[j]+=p;
			System.out.println(nombre[j]+" pidió "+puntos[j][0]+" e hizo "+puntos[j][1]+" Puntos="+p+" Total:"+puntosPartida[j]);
		}
		
		jugadorMano++; 
		if (jugadorMano==nJugadores) {jugadorMano=0;}
	}
	
	public static boolean comprueba(int c,int cGanadora, int paloBaza, int pinte, int[] cartas) {
		if (cGanadora==-1)return true;
		boolean ok=true;
		int palo=c/10;
		boolean puedeSeguirPalo=false;
		boolean puedeMatarPalo=false;
		boolean tieneTriunfoGanador=false;
		for (int n=1;n<=cartas[0];n++) {	
			if (	(cartas[n]/10==pinte&&cGanadora/10!=pinte)
					||
					(cartas[n]/10==pinte&&cGanadora/10==pinte&&corrigeValor(cGanadora%10)<corrigeValor(cartas[n]%10))
					||
					(cartas[n]/10==pinte&&corrigeValor(cGanadora%10)<corrigeValor(cartas[n]%10))
					) {
				tieneTriunfoGanador=true;
				}
			
			if (cartas[n]/10==paloBaza) {
				puedeSeguirPalo=true;
				}
			
			if (cartas[n]/10==paloBaza&&corrigeValor(cGanadora%10)<corrigeValor(cartas[n]%10)) {
				puedeMatarPalo=true;
				}
		}
		if (palo!=paloBaza&&puedeSeguirPalo) {ok=false;System.out.println("Error: Tienes "+translatePalo(paloBaza));}else
			//and paloganadora==paloBaza (si no es que han tirado triunfo)
		if (palo==paloBaza&&puedeMatarPalo&&corrigeValor(c%10)<corrigeValor(cGanadora%10)) {ok=false;System.out.println("Error: Puedes matar el "+translate(cGanadora));}else
		if (palo!=paloBaza&&palo!=pinte&&tieneTriunfoGanador) {ok=false;System.out.println("Error: Tienes que matar con el triunfo...");}
		
		return ok;
	}
	public static int compara(int c1, int c2, int paloPinte, int paloBaza) {
		System.out.println(translate(c1)+" vs "+translate(c2)+" siendo el palo de baza "+translatePalo(paloBaza)+" y pintan "+translatePalo(paloPinte));
		
		int nc1=c1%10;
		nc1=corrigeValor(nc1);
				
		int pc1=c1/10;
		
		if (pc1==paloPinte) {nc1=nc1*1000;}else if (pc1==paloBaza) {nc1=nc1*100;}
		
		int nc2=c2%10;
		
		nc2=corrigeValor(nc2);
				
		int pc2=c2/10;
		if (pc2==paloPinte) {nc2=nc2*1000;}else if (pc2==paloBaza) {nc2=nc2*100;}
		
		return (nc1>nc2?c1:c2);
	}
	
	public static int corrigeValor(int valorCarta) {
		int valorCorregido=valorCarta;
		if (valorCarta==0) {valorCorregido=10;}else
			if (valorCarta==2) {valorCorregido=9;}else
				if(valorCarta>2) {valorCorregido=valorCarta-1;}
	return valorCorregido;
	}
	
	public static String[] inkey() {
		String[] command= {"","",""};
		
		String s=in.nextLine();
        command = s.split(" ");
 		
		return command;
	}
	
	public static void main(String[] args) {
		in=new Scanner(System.in);
		// TODO Auto-generated method stub
		partida(5);
		Runtime.getRuntime().halt(0);
		
		for (int i=0;i<40; i++) {
		  System.out.println(i+"-"+translate(dameCarta()));
		  
		  }		
		String[] command= inkey();
		while (!command[0].equals("exit")){
			status();
			command=inkey();
			//comandos de 2 palabras 
            
            if ((command.length==2)&&estado.equals("Esperando")) {
            
            if (command[0].equals("alta")) {
            	jugador[nJugadores]= new Jugador(command[1],nJugadores);
            	jugador[nJugadores].estado="Esperando";
            	nJugadores++;
            }
            
            
            
            if (command[0].equals("empezar")) {
            	int i=indice(command[1]);
            	if (i>-1) {
            	jugador[i].estado="Listo";
            	}
            }
            }
		
            boolean empezamos=true;
            for (int i=0;i<nJugadores; i++) {
            	if (!jugador[i].estado.equals("Listo")) {
            		empezamos=false;
            	}
          
            }
            if (empezamos) {estado="Empezamos";}
           
            if (estado.equals("Empezamos")) {
            	mazo=baraja(mazo);
            	for (int i=0;i<nJugadores; i++) {
            		jugador[i].cartas[0]++;
            		jugador[i].cartas[jugador[i].cartas[0]]=mazo[i];
            		
            	}
            	
            	
            }
            
            
            
		}
		
		for (int i=0;i<40; i++) {
			System.out.println(translate(mazo[i]));
		}
		in.close();
	}
	
	

	public static void status() {
		System.out.println ("Servidor Santa Mónica v1.0");
		System.out.println ("Estado:"+estado);
		System.out.println ("Jugadores:"+nJugadores);
		for (int n=0;n<nJugadores;n++) {
			System.out.println(jugador[n].nombre+"("+jugador[n].estado+")");
			for (int m=0;m<jugador[n].cartas[0];m++) {
				System.out.print(translate(jugador[n].cartas[m+1])+",");
			}System.out.println();
		}
		System.out.println();
	}
	
	
	public static int[] baraja(int[] m) {
		Random rnd = new Random();
		for (int i=0;i<40; i++) {
			m[i]=i;
		}
		for (int i=0;i<40;i++) {
			int c=rnd.nextInt(40-i);
			int a=m[i];
			m[i]=m[c+i];
			m[c+i]=a;
		}
		m[40]=40;
		return m;
	}
	
	
	public static int dameCarta() {
		int carta=-1;
		if (mazo[40]>0) {
			mazo[40]--;
			carta=mazo[mazo[40]];
			}
		return carta;
	}
	public static int indice(String nombre) {
		int indice=-1;
		for (int i=0;i<nJugadores;i++) {
			if (nombre.equals(jugador[i].nombre)) {indice=i;}
		}
		return indice;
	}
	
	public static String translatePalo(int palo) {
		String paloS=(palo==0?"Oros":(palo==1?"Copas":(palo==2?"Espadas":(palo==3?"Bastos":"Error"))));
		return paloS;
	}
	
	public static String translate(int i) {
		if ((i<0)||(i>39)) {
			return "Error";
		}else {
			int palo=i/10;
			int numero=i % 10;
			String numeroS=(numero==0?"As":(numero==1?"Dos":(numero==2?"Tres":(numero==3?"Cuatro":(numero==4?"Cinco":(numero==5?"Seis":(numero==6?"Siete":(numero==7?"Sota":(numero==8?"Caballo":(numero==9?"Rey":"Error"))))))))));
			String paloS=translatePalo(palo);
			return (numeroS+" de "+paloS);
			}
	}
}
