package fr.etu.ea.model.operators.mutation;

import fr.etu.ea.model.Individu;

public class NoMutation implements IMutation {

	@Override
	public void mutate(Individu individu) {
		//Mutation identité (ne fait rien)
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
