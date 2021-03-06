package grisu.gricli.command;

import grisu.control.JobConstants;
import grisu.control.ServiceInterface;
import grisu.control.exceptions.NoSuchJobException;
import grisu.gricli.GricliEnvironment;
import grisu.gricli.GricliRuntimeException;
import grisu.gricli.completors.JobPropertiesCompletor;
import grisu.gricli.completors.JobnameCompletor;
import grisu.gricli.util.ServiceInterfaceUtils;
import grisu.model.dto.DtoJob;

import java.util.Map;

public class PrintJobCommand implements
GricliCommand {
	private final String jobname;
	private final String attribute;

	@SyntaxDescription(command={"print","jobs"})
	public PrintJobCommand(){
		this("*","status");
	}

	@SyntaxDescription(command={"print","job"},arguments={"jobname"})
	@AutoComplete(completors={JobnameCompletor.class})
	public PrintJobCommand(String jobname){
		this(jobname,null);
	}

	@SyntaxDescription(command={"print","job"},arguments={"jobname","attribute"})
	@AutoComplete(completors={JobnameCompletor.class,JobPropertiesCompletor.class})
	public PrintJobCommand(String jobname, String attribute) {
		this.jobname = jobname;
		this.attribute = attribute;
	}

	public GricliEnvironment execute(GricliEnvironment env)
	throws GricliRuntimeException {
		ServiceInterface si = env.getServiceInterface();
		for (String j : ServiceInterfaceUtils.filterJobNames(si, jobname)) {
			try {
				if (attribute != null) {
					printJobAttribute(env,si, j, attribute);
				} else {
					printJob(env,si, j);
				}
			} catch (NoSuchJobException ex) {
				throw new GricliRuntimeException("job " + j + " does not exist");
			}
		}

		return env;
	}

	private void printJob(GricliEnvironment env, ServiceInterface si, String j)
	throws NoSuchJobException {
		DtoJob job = si.getJob(j);
		env.printMessage("Printing details for job " + jobname);
		env.printMessage("status: "
				+ JobConstants.translateStatus(si.getJobStatus(jobname)));
		Map<String, String> props = job.propertiesAsMap();
		for (String key : props.keySet()) {
			env.printMessage(key + " : " + props.get(key));
		}
	}

	private void printJobAttribute(GricliEnvironment env, ServiceInterface si, String j,
			String attribute) throws NoSuchJobException {
		DtoJob job = si.getJob(j);
		if (!("status".equals(attribute))) {
			env.printMessage(j + " : " + job.jobProperty(attribute));
		} else {
			env.printMessage(j + " : "
					+ JobConstants.translateStatus(si.getJobStatus(j)));
		}
	}

}
