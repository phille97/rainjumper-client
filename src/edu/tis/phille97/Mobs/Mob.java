package edu.tis.phille97.Mobs;

import edu.tis.phille97.Exceptions.ShittyParameterException;
import java.awt.Graphics2D;

public class Mob {
	
        protected double HP = 0;
	private String name = "(No name)";
	public String getName(){
		return this.name;
	}
        public double getHP(){
            return this.HP;
        }
        public void hit(int amountReduced) throws ShittyParameterException {
            if(amountReduced > 0) this.HP -= amountReduced;
            else throw new ShittyParameterException("Param: " + this.HP + ", Should be larger than 0");
        }
        public void heal(int amount) throws ShittyParameterException {
            if(amount > 0) this.HP += amount;
            else throw new ShittyParameterException("Param: " + this.HP + ", Should be larger than 0");
        }
	
	public void tick(){
		
	}
	
	public void print(Graphics2D g){
		
	}
}
