package dev.pluginevents;

import dev.PluginHostCommunicator;

public class PluginError extends PluginEvent {
	private PluginHostCommunicator sourceHost;
	private Exception exception;

	public PluginError(Exception exception, PluginHostCommunicator sourceHost) {
		this.sourceHost = sourceHost;
		this.exception = exception;
	}

	public PluginHostCommunicator getSourceHost() {
		return this.sourceHost;
	}

	public Exception getException() {
		return this.exception;
	}
}
