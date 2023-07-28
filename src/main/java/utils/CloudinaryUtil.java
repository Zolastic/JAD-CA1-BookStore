package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUtil {
	
	static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
			  "cloud_name", "dnjd1wruf",
			  "api_key", "213785792351827",
			  "api_secret", "xNDc2eA9eBf6ey99w5cHxo2dvaA"));
	
	public static String uploadImage(byte[] imageData) {
		Map params = ObjectUtils.asMap("folder", "", "resource_type", "image");
		try {
			Map result = cloudinary.uploader().upload(imageData, params);
			String publicID = (String) result.get("public_id");
			System.out.println(publicID);
			return publicID;
		} catch (Exception exception) {
			exception.printStackTrace();
			return "error";
		}
	}
	
	public static String getImage(String publicID) {
		try {
			Map resourceInfo = cloudinary.api().resourcesByIds(Arrays.asList(publicID), ObjectUtils.emptyMap());
			System.out.println(resourceInfo);
			List<Map<String, Object>> resources = (List<Map<String, Object>>) resourceInfo.get("resources");
			String secureURL = null;
			if (resources != null && !resources.isEmpty()) {
			    Map<String, Object> resource = resources.get(0);
			    secureURL = (String) resource.get("secure_url");
			}
			return secureURL;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	public static int deleteImageFromCld(String publicID) {
		try {
			   ApiResponse apiResponse = cloudinary.api().deleteResources(Arrays.asList(publicID),
			      ObjectUtils.asMap("type", "upload", "resource_type", "image"));
			  System.out.println(apiResponse);
			  return 200;
			} catch (Exception exception) {
			  System.out.println(exception.getMessage());
			  return 500;
			}
	}

}
