package grisu.gricli.command;


import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.gricli.Gricli;
import grisu.gricli.GricliEnvironment;
import grisu.gricli.GricliRuntimeException;
import grisu.gricli.completors.BackendCompletor;
import grisu.gricli.completors.CompletionCache;
import grisu.gricli.completors.CompletionCacheImpl;
import grisu.model.GrisuRegistryManager;


public class InteractiveLoginCommand implements
GricliCommand {
	private final String backend;

	@SyntaxDescription(command={"ilogin"},arguments={"backend"})
	@AutoComplete(completors={BackendCompletor.class})
	public InteractiveLoginCommand(String backend) {
		this.backend = backend;
	}

	public GricliEnvironment execute(GricliEnvironment env)
			throws GricliRuntimeException {
		try {
			ServiceInterface si = LoginManager.loginCommandline(backend);
			env.setServiceInterface(si);

			CompletionCache cc = new CompletionCacheImpl(env);
			GrisuRegistryManager.getDefault(si).set(
					Gricli.COMPLETION_CACHE_REGISTRY_KEY, cc);

			Gricli.completionCache = cc;

			// CompletionCache.jobnames = si.getAllJobnames(null).asSortedSet();
			// CompletionCache.fqans = si.getFqans().asSortedSet();
			// CompletionCache.queues = si.getAllSubmissionLocations()
			// .asSubmissionLocationStrings();
			// CompletionCache.sites = si.getAllSites().asArray();

			return env;
		} catch (LoginException ex) {
			throw new GricliRuntimeException(ex);
		}
	}

}
