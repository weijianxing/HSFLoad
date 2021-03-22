package com.wuage.test.loading;

import com.wuage.search.share.dto.OfferDTO;
import com.wuage.search.share.dto.SearchOfferDTO;
import com.wuage.search.share.dto.SpuDTO;
import com.wuage.search.share.model.SpuDO;
import com.wuage.search.share.result.OfferSearchPageResult;
import com.wuage.search.share.service.OfferQueryService;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.List;

/**
 * Created by hacke on 017/7/11.
 */
public class LSearch extends AbstractJavaSamplerClient {
    private static OfferQueryService offerQueryService = null;
    private static Arguments arguments = null;
    private String keyword;
    private final static int PAGE_SIZE=60;
    @Override
    public Arguments getDefaultParameters() {
        arguments = new Arguments();
        arguments.addArgument("keyword","");
        return arguments;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        offerQueryService = (OfferQueryService)InitHSF.getInstance().getBean("offerQueryService");
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        super.teardownTest(context);
    }



    public void setParamters(JavaSamplerContext javaSamplerContext){
        keyword=javaSamplerContext.getParameter("keyword");
    }

    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        setParamters(javaSamplerContext);
        SearchOfferDTO query = new SearchOfferDTO();
//        set search keyword
        query.setSearchWord(keyword);
        query.setPageSize(PAGE_SIZE);

        OfferSearchPageResult<OfferDTO> response= null;
        sampleResult.sampleStart();
        try {
            if(offerQueryService != null){
                response = offerQueryService.searchOfferList(query);
            }else{
                sampleResult.setSuccessful(false);
                System.err.println("init bean fail.");
                return sampleResult;
            }
            if (response!=null) {

                List<OfferDTO> offerList= response.getModel();
                List<SpuDO> spuList= response.getSpuList();
                int totalOffer= response.getCount();
                if(offerList!=null && spuList!=null && spuList.size()>0 && offerList.size()>0 && totalOffer>0){
                    sampleResult.setSuccessful(true);
                    this.getLogger().info("total call back offers : " + totalOffer);
                }else{
                    sampleResult.setSuccessful(false);
                    this.getLogger().error("search no data response. keyword is : " + keyword);
                }

            }else {
                this.getLogger().error("response is null keyword is : "  +keyword);
                sampleResult.setSuccessful(false);

            }
        }catch (Exception e) {
            this.getLogger().error(e.getStackTrace().toString());
//            sampleResult.setResponseMessage("getMessage :" + e.getMessage() + "\ngetStackTrace:" + e.getStackTrace());
            sampleResult.setSuccessful(false);
        }finally {
            sampleResult.sampleEnd();

        }
        return sampleResult;

    }
}
