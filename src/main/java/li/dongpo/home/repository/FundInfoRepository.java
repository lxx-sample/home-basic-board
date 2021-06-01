package li.dongpo.home.repository;

import li.dongpo.home.model.FundInfo;

import java.util.List;

/**
 * @author dongpo.li
 * @date 2021/5/27
 */
public interface FundInfoRepository {

    FundInfo findById(long id);

    List<FundInfo> findAll();

}
