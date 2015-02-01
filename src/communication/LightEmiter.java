package communication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;

public class LightEmiter {
	private static final char VALUE_ON = 'h';
	private static final char VALUE_OFF = 'l';
	private HASocket haSocket;

	public LightEmiter(HASocket haSocket){
		this.haSocket=haSocket;

		setListeners();
	}

	private void setListeners() {
		haSocket.getSocket().on("lightStateChanged" ,new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject json = (JSONObject) args[0];
				try {
					HASocket.getMainFrame().lightStateChange(json.getInt("id"), json.getBoolean("isOn"));
					if(json.getInt("id") == 1){
						HASocket.driver.say('l');
						if(json.getBoolean("isOn")){
							HASocket.driver.say(VALUE_ON);
						}else{
							HASocket.driver.say(VALUE_OFF);
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).on("resLampesStates", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("light states");
				JSONArray arg = (JSONArray)args[0];
				for(int i = 0; i < arg.length();i++){
					JSONObject json;
					try {
						json = arg.getJSONObject(i);
						HASocket.getMainFrame().lightStateChange(json.getInt("id"), json.getBoolean("isOn"));
						if(json.getInt("id") == 1){
							HASocket.driver.say('l');
							if(json.getBoolean("isOn")){
								HASocket.driver.say(VALUE_ON);
							}else{
								HASocket.driver.say(VALUE_OFF);
							}
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				if(haSocket.isJustConnected()){
					haSocket.setJustConnected( false );
				}

				HASocket.getMainFrame().setVisible(true);

			}
		});
	}

	public void getLightStates(){
		haSocket.getSocket().emit("reqLampesStates");
	}

}
