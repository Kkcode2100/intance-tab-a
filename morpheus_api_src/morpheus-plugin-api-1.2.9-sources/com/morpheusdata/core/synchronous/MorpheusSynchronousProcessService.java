package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Process;
import com.morpheusdata.model.ProcessEvent;
import com.morpheusdata.model.ProcessStepType;
import com.morpheusdata.model.ProcessStepUpdate;
import com.morpheusdata.model.User;
import com.morpheusdata.model.Workload;

public interface MorpheusSynchronousProcessService extends MorpheusSynchronousDataService<Process, Process> {

	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param stepType the ProcessStepType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @return Boolean indicating success
	 */
	Process startProcess(Workload workload, ProcessStepType stepType, User user, String timerCategory);

	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param stepType the ProcessStepType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @param eventTitle an event title to associate with this Process
	 * @return Boolean indicating success
	 */
	Process startProcess(Workload workload, ProcessStepType stepType, User user, String timerCategory, String eventTitle);

	/**
	 * Start a new ProcessEvent associated to the Process. This will end any currently running
	 * ProcessEvents associated to the Process
	 * @param process The Process on which to create a new ProcessEvent to start
	 * @param nextEvent The new ProcessEvent to start
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @return Boolean indicating success
	 */
	Boolean startProcessStep(Process process, ProcessEvent nextEvent, String processStatus);

	/**
	 * Start a new ProcessEvent associated to the Process while ending a specific previous step.
	 * This will end the ProcessEvent with the specified lastStepType and then start a new ProcessEvent.
	 * @param process The Process on which to create a new ProcessEvent to start
	 * @param lastStepType The type of the last step to end before starting the new event
	 * @param nextStep The new ProcessEvent to start
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @return Boolean indicating success
	 */
	Boolean startProcessStepAt(Process process, ProcessStepType lastStepType, ProcessEvent nextStep, String processStatus);

	/**
	 * Updates the last ProcessEvent of type `lastStepType`
	 * @param process The process to search for an event of type `lastStepType`
	 * @param lastStepType type of the last event to update
	 * @param stepUpdate update parameters for the event
	 * @param appendOutput true to append to the existing output; false to replace
	 * @return Boolean indicating success
	 */
	Boolean updateProcessStep(Process process, ProcessStepType lastStepType, ProcessStepUpdate stepUpdate, Boolean appendOutput) ;

	/**
	 * Stops the last ProcessEvent associated with the Process
	 * <p>
	 * This will not appendOutput and will instead override it. If you want to preserve the existing output, use
	 * {@link #endProcessStep(Process, String, String, Boolean)} which allows you to specify appendOutput = true.
	 *
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the ProcessEvent
	 * @return Boolean indicating success
	 */
	Boolean endProcessStep(Process process, String processStatus, String output);

	/**
	 * Stops the last ProcessEvent associated with the Process
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the ProcessEvent
	 * @param appendOutput true to append to the existing output; false to replace
	 * @return Boolean indicating success
	 */
	Boolean endProcessStep(Process process, String processStatus, String output, Boolean appendOutput);


	/**
	 * Stops a specific ProcessEvent associated with the Process.
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param lastStepType The type of the last step to end
	 * @param processStatus The status (i.e. 'complete', 'failed'). If not specified, 'complete' is used.
	 * @param output The output to associate with the ProcessEvent
	 * @param appendOutput true to append to the existing output; false to replace
	 * @return Boolean indicating success
	 */
	Boolean endProcessStepAt(Process process, ProcessStepType lastStepType, String processStatus, String output, Boolean appendOutput);

	/**
	 * End the process with the status specified
	 * @param process The Process to end
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the Process
	 * @return Boolean indicating success
	 */
	Boolean endProcess(Process process, String processStatus, String output);
}
