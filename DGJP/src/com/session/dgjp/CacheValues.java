package com.session.dgjp;

import java.util.List;

import android.support.v4.util.LongSparseArray;

import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.City;

/** 保存在内存的静态变量 */
public class CacheValues {

	private static List<City> cities;
	private static LongSparseArray<List<BranchSchool>> branchSchools;

	public static List<City> getCities() {
		return cities;
	}

	public static void setCities(List<City> cities) {
		CacheValues.cities = cities;
	}
	
	public static List<BranchSchool> getBranchSchools(long cityId) {
		if (branchSchools != null) {
			List<BranchSchool> list = branchSchools.get(cityId);
			if (list != null && list.size() > 0) {
				return list;
			}
		}
		return null;
	}
	
	public static void setBranchSchools(long cityId, List<BranchSchool> schools) {
		if (branchSchools == null) {
			branchSchools = new LongSparseArray<List<BranchSchool>>();
		}
		branchSchools.put(cityId, schools);
	}

}
