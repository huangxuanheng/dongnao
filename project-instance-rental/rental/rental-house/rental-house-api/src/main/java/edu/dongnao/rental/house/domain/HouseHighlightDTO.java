package edu.dongnao.rental.house.domain;

import java.io.Serializable;

/**
 *
 * 房源信息高亮信息类
 * 
 */
public class HouseHighlightDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long houseId;

    private String title;

    private String subwayLineName;

    private String subwayStationName;

    private String district;

    private String traffic;

    private String roundService;
    
    private String layoutDesc;
    
    private String description;

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSubwayLineName() {
        return subwayLineName;
    }

    public void setSubwayLineName(String subwayLineName) {
        this.subwayLineName = subwayLineName;
    }

    public String getSubwayStationName() {
        return subwayStationName;
    }

    public void setSubwayStationName(String subwayStationName) {
        this.subwayStationName = subwayStationName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getRoundService() {
        return roundService;
    }

    public void setRoundService(String roundService) {
        this.roundService = roundService;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLayoutDesc() {
		return layoutDesc;
	}

	public void setLayoutDesc(String layoutDesc) {
		this.layoutDesc = layoutDesc;
	}
    
}
