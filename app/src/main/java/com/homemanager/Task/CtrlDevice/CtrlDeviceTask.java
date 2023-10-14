package com.homemanager.Task.CtrlDevice;
import com.homemanager.Task.Task;
import org.json.JSONObject;


public class CtrlDeviceTask extends Task{
    private CtrlDeviceMessage ctrlDeviceMessage;
    private CtrlDeviceObject ctrlDeviceObject;
    private boolean validData;

    public CtrlDeviceTask(CtrlDeviceMessage ctrlDeviceMessage) {
        super();
        this.ctrlDeviceMessage = ctrlDeviceMessage;
        this.ctrlDeviceObject = new CtrlDeviceObject();
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public void setDuration(long duration) {
    }

    @Override
    public int getTaskDescriptor() {
        return 789;
    }

    @Override
    public void parseContent(JSONObject content) {
        ctrlDeviceObject.createCtrlDeviceObject(content);
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            //TODO : GetCtrlDevices instead of GetRooms
            jsonParams.put("action", "GetRooms");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public void inDoneStateNotification(){
        ctrlDeviceMessage.displayCtrlDevice(ctrlDeviceObject);
    }
}
