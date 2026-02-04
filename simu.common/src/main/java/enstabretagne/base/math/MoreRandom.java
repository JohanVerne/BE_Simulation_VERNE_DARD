/**
* Classe MoreRandom.java
*@author Olivier VERRON
*@version 1.0.
*/
/**
 * Fournit les classes n�cessaires pour effectuer
 * des simulations � �v�nements discrets basiques.
 * @author 	Pascal CANTOT (cantot@wanadoo.fr]
 * @version	1.0
 */
package enstabretagne.base.math;

import java.util.Random;
import java.lang.System;


// TODO: Auto-generated Javadoc
/**
 *  Etend les fonctions de g�n�ration de nombre al�atoires.
 *  Fournit notamment des g�n�rateurs suivant diff�rentes lois.
 *
 * @author cantot@wanadoo.fr
 * @see Random
 */
public class MoreRandom extends Random {

	
		/** The Constant serialVersionUID. */
		public static final long serialVersionUID = 1L;

		/** The global seed. */
		public static long globalSeed=0;
		
		/** The initial seed. */
		private static long initialSeed = 0L;
		
		/**
		 * Constructeur par d�faut.
		 * Le g�n�rateur al�atoire sera initialis� avec un germe "al�aloire" (horloge syst�me)
		 */
		public MoreRandom()
		{
			
			super();
			initialSeed = globalSeed;
		}

		/**
		 * Constructeur initialis� avec un germe al�atoire explicit�.
		 * @param seed germe.
		 */
		public MoreRandom(long seed)
		{
			super(seed);
			initialSeed = seed;
		}

		/**
		 * R�initialise le germe � la valeur indiqu�e.
		 * @param seed germe.
		 */
		public void setSeed(long seed)
		{
			super.setSeed(seed);
			initialSeed = seed;
		}

		/**
		 * Retourne le germe initial.
		 * @return germe.
		 */
		public long getSeed()
		{
			return initialSeed;
		}

		/**
		 * Initialise le germe � une valeur al�atoire,
		 * construite � partir de l'heure syst�me.
		 * Fournit une valeur de germe diff�rente du germe pr�c�dent � chaque ex�cution,
		 * m�me lors de deux appels se succ�dant � moins d'une milliseconde d'�cart.
		 * @return la valeur du germe.
		 */
		public long randomize()
		{
			long newSeed;
			
			// Boucle pour �viter d'avoir un germe identique si appels trop rapproch�s.
			// Inconv�nient: prend jusqu'� 1ms maxi � s'ex�cuter (rarement)
			// Avantage: tr�s rapide dans la plupart des cas
			// (donc peut-�tre mieux qu'un thread.sleep(1);)
			do
			{
				newSeed = System.currentTimeMillis();
			} while (newSeed == initialSeed);
			setSeed(newSeed);
			return newSeed;		
		}

		/**
		 * G�n�rateur pseudo-al�atoire suivant la loi uniforme sur [0,1].
		 * @return un nombre pseudo-al�atoire entre 0.0 et 1.0, suivant la loi uniforme
		 */
		public double nextUniform()
		{
			return super.nextDouble();
		}
		
		/**
		 * G�n�rateur pseudo-al�atoire suivant la loi uniforme sur [a,b].
		 *
		 * @param a the a
		 * @param b the b
		 * @return un nombre pseudo-al�atoire entre <code>a</code> et <code>b</code>,
		 * 			suivant la loi uniforme
		 */
		public double nextUniform(double a, double b)
		{
			return a + (b-a)*nextDouble();
		}

		/**
		 * G�n�rateur pseudo-al�atoire suivant la loi triangulaire.
		 * @param	a	borne inf�rieure;
		 * @param	b	abscisse de la densit� de probabilit� maximale;
		 * @param	c	borne sup�rieure.
		 * @return	un nombre pseudo-al�atoir entre <code>a</code> et <code>c</code>
		 * 			suivant une loi triangulaire(a,b,c).
		 */
		public double nextTriangle(double a, double b, double c)
		{
			double beta;
			double t;
			double u;

			// M�thode de la transform�e inverse
			u = nextUniform();
			beta = (b-a)/(c-a);
			if (u < beta)
				t = Math.sqrt(beta*u);
			else
				t = 1.0 - Math.sqrt((1-beta)*(1-u));

			return a + (c-a)*t;
		}

		/**
		 * G�n�rateur pseudo-al�atoire suivant la loi exponentielle.
		 * @param	lambda	param�tre de la loi
		 * @return	un nombre pseudo-al�atoir suivant la loi exponentielle.
		 */
		public double nextExp(double lambda)
		{
			return (-1.0/lambda)*Math.log(1-nextUniform());
		}
		
		/**
		 * Do test.
		 */
		private static void doTest()
		{
			int i;
			MoreRandom alea = new MoreRandom();
			
			System.out.println("*** TEST CLASSE moreRandom ***");
			
			// Teste les g�n�rateurs
			System.out.print("Loi uniforme [0;1]: ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();
			
			System.out.print("Loi uniforme [-10;10]: ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform(-10.0,10.0));
			System.out.println();
			
			System.out.print("Apr�s changement de germe: ");
			alea.setSeed(1234L);
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Apr�s randomize: ");
			alea.randomize();
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Apr�s randomize (bis): ");
			alea.randomize();
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Apr�s reprise du germe: ");
			alea.setSeed(1234L);
			System.out.println("(germe:" + alea.getSeed()+ ")");
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextUniform());
			System.out.println();

			System.out.print("Loi triangulaire(0,5,10): ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextTriangle(0,5,10));
			System.out.println();
			
			System.out.print("Loi exponentielle(1.0/10.0): ");
			System.out.println();
			for (i=0; i<10; i++)
				System.out.print(" " + alea.nextExp(1.0/10.0));
			System.out.println();
			
		}

		/**
		 * The main method.
		 *
		 * @param args the arguments
		 */
		public static void main(String[] args)
		{
			doTest();
		}

		public int nextInt(int origin, int bound) {
			return nextInt(bound)+origin;
		}

		

		
}  // Class MoreRandom




