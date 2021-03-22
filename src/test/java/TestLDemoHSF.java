
import com.wuage.search.share.dto.OfferDTO;
import com.wuage.search.share.dto.SearchOfferDTO;
import com.wuage.search.share.dto.SellerDTO;
import com.wuage.search.share.model.SpuDO;
import com.wuage.search.share.result.OfferSearchPageResult;
import com.wuage.search.share.service.OfferQueryService;
import com.wuage.search.share.service.SellerQueryService;
import com.wuage.test.loading.InitHSF;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by hacke on 2017/5/11.
 */
public class TestLDemoHSF {
    private static OfferQueryService offerQueryService = null;
    private static SellerQueryService sellerQueryService = null;


    @Before
    public void setupTest() {
        System.out.println("begin init ");
        offerQueryService = (OfferQueryService)InitHSF.getInstance().getBean("offerQueryService");
//        sellerQueryService = (SellerQueryService)InitHSF.getInstance().getBean("sellerQueryService");
    }





    public void setParamters(JavaSamplerContext javaSamplerContext){


    }

    @Test
    public void runTest() {
        SampleResult sampleResult = new SampleResult();
        SearchOfferDTO query = new SearchOfferDTO();
//        set search keyword
        query.setSearchWord("");
        query.setPageSize(60);

        OfferSearchPageResult<OfferDTO> response= null;
        sampleResult.sampleStart();
//        if(sellerQueryService !=null )
//            try {
//                SellerDTO sellerDTO = sellerQueryService.getSeller("不锈钢");
//                System.out.println(""+sellerDTO);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        System.out.println("test ing.");
        try {
            if(offerQueryService != null){
                response = offerQueryService.searchOfferList(query);
            }else{
                sampleResult.setSuccessful(false);
                System.err.println("init bean fail.");
                return ;
            }
            if (response!=null) {

                List<OfferDTO> offerList= response.getModel();
                List<SpuDO> spuList= response.getSpuList();
                int totalOffer= response.getCount();
                if(offerList!=null && spuList!=null && spuList.size()>0 && offerList.size()>0 && totalOffer>0){
                    sampleResult.setSuccessful(true);
                    System.out.println("total call back offers : " + totalOffer);
                }else{
                    sampleResult.setSuccessful(false);
                    System.out.println("search no data response.");
                }

            }else {
                System.out.println("response is null");
                sampleResult.setSuccessful(false);

            }
        }catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
//            sampleResult.setResponseMessage("getMessage :" + e.getMessage() + "\ngetStackTrace:" + e.getStackTrace());
            sampleResult.setSuccessful(false);
        }finally {
            sampleResult.sampleEnd();

        }
    }
}
