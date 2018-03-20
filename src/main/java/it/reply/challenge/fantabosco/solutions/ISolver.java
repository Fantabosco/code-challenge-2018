package it.reply.challenge.fantabosco.solutions;

import it.reply.challenge.fantabosco.model.Provider;
import it.reply.challenge.fantabosco.model.Project;

import java.util.List;

@FunctionalInterface
public interface ISolver {
	
	public abstract List<Project> solve(List<Provider> providers,List<String> services,
			List<String> countries,List<Project> projects);
}
