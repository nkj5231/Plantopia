package plantopia.sungshin.plantopia.Search;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import plantopia.sungshin.plantopia.Plant.PlantItem;
import plantopia.sungshin.plantopia.R;

public class SearchFragment extends android.support.v4.app.Fragment {
    private Unbinder unbinder;

//    @BindView(R.id.search_list)
    ListView searchListView;
//    @BindView(R.id.search_edit_text)
    EditText editText;
//    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    SearchListAdapter adapter = new SearchListAdapter();

    static final String KEY = "20180814WAQFXYCPVL972GCN79KFQ";
    static final String IMG_PATH1 = "http://www.nongsaro.go.kr/cms_contents/301/";
    static final String IMG_PATH2 = "_MF_REPR_ATTACH_01_TMB.jpg";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        unbinder = ButterKnife.bind(this, view);

        searchListView = view.findViewById(R.id.search_list);
        progressbar = view.findViewById(R.id.progressbar);
        editText = view.findViewById(R.id.search_edit_text);

        progressbar.setVisibility(View.VISIBLE);
        getPlantDataTask ayncTask = new getPlantDataTask();
        ayncTask.execute();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                ((SearchListAdapter) searchListView.getAdapter()).getFilter().filter(filterText);
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlantItem plant = (PlantItem) adapter.getItem(position);
                String name = plant.getPlant_name();
                String number = plant.getPlantNumber();
                String image = plant.getPlant_img();

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("number", number);
                intent.putExtra("image", image);

                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class getPlantDataTask extends AsyncTask<String, Void, HttpResponseCache> {
        @Override
        protected HttpResponseCache doInBackground(String... strings) {

            HttpResponseCache response = null;
            final String apiurl = "http://api.nongsaro.go.kr/service/garden/gardenList";
            HttpURLConnection conn = null;
            try {
                StringBuffer sb = new StringBuffer(3);
                sb.append(apiurl);
                sb.append("?apiKey=" + KEY);
                sb.append("&numOfRows=300");

                String query = sb.toString();
                URL url = new URL(query);
                conn = (HttpURLConnection) url.openConnection();
                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                byte[] bytes = new byte[4096];
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while (true) {
                    int red = in.read(bytes);
                    if (red < 0) break;
                    baos.write(bytes, 0, red);
                }

                String xmlData = baos.toString("utf-8");
                baos.close();
                in.close();
                conn.disconnect();
                Document doc = docBuilder.parse(new InputSource(new StringReader(xmlData)));
                Element el = (Element) doc.getElementsByTagName("items").item(0);

                for (int i = 0; i < ((Node) el).getChildNodes().getLength(); i++) {
                    Node node = ((Node) el).getChildNodes().item(i);
                    if (!node.getNodeName().equals("item")) {
                        continue;
                    }
                    String plantNum = node.getChildNodes().item(0).getFirstChild().getNodeValue();
                    String plantName = node.getChildNodes().item(1).getFirstChild().getNodeValue();
                    String plantImgPath = IMG_PATH1 + plantNum + IMG_PATH2;

                    adapter.addPlant(plantName, plantNum, plantImgPath);
                }
                publishProgress();

            } catch (Exception e) {
                Log.i("검색탭", "doInBackground: 파싱 오류");
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) conn.disconnect();
                } catch (Exception e) {
                }
            }

            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            try {
                searchListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}