package view.comp;

import com.LabelService;
import com.MeetingService;
import model.Cache;
import view.dialog.*;

@SuppressWarnings("all")
public class DialogFactory {

    private DialogFactory() {}

    public static MeetingDialog createMeetingDialog(MeetingService meeetingService, Cache cache) {
        return new CreateMeetingDialog(meeetingService, cache);
    }

    public static MeetingDialog modifyMeetingDialog(MeetingService meetingService, Cache cache) {
        return new ModifyMeetingDialog(meetingService, cache);
    }

    public static LabelDialog createLabelDialog(LabelService service){
        return new CreateLabelDialog(service);
    }

    public static LabelDialog modifyLabelDialog(LabelService service){
        return new ModifyLabelDialog(service);
    }
}
