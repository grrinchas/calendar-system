package cont;

import model.Cache;

@SuppressWarnings("all")
public interface CacheListener {

  void onMeetingChange(Cache cache);

  void onLabelChange(Cache cache);

}
