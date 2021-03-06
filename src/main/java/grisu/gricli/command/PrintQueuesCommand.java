package grisu.gricli.command;

import java.util.ArrayList;
import java.util.Arrays;

import grisu.control.ServiceInterface;
import grisu.gricli.GricliEnvironment;
import grisu.gricli.GricliRuntimeException;
import grisu.gricli.completors.FqanCompletor;
import grisu.model.dto.DtoSubmissionLocations;


public class PrintQueuesCommand implements
GricliCommand {
	private final String fqan;

	@SyntaxDescription(command={"print","queues"})
	public PrintQueuesCommand(){
		this(null);
	}

	@SyntaxDescription(command={"print","queues"}, arguments={"group"})
	@AutoComplete(completors={FqanCompletor.class})
	public PrintQueuesCommand(String fqan) {
		this.fqan = fqan;
	}

	public GricliEnvironment execute(GricliEnvironment env)
			throws GricliRuntimeException {
		ServiceInterface si = env.getServiceInterface();
		DtoSubmissionLocations queues = (fqan == null) ? si
				.getAllSubmissionLocations() : si
				.getAllSubmissionLocationsForFqan(fqan);

		String[] queueStrings = queues.asSubmissionLocationStrings();
		Arrays.sort(queueStrings);

		for (String queue : queues.asSubmissionLocationStrings()) {
			env.printMessage(queue);
		}
		return env;
	}

}
