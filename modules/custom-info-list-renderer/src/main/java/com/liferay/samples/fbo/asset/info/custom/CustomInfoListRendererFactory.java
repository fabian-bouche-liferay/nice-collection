package com.liferay.samples.fbo.asset.info.custom;

import com.liferay.info.item.renderer.InfoItemRendererRegistry;
import com.liferay.info.list.renderer.InfoListRenderer;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		configurationPid = CustomInfoListRendererConfigurationConstants.CONFIGURATION_ID,
		service = CustomInfoListRendererFactory.class 
		)
public class CustomInfoListRendererFactory {

	private static final String DYNAMIC_INFO_LIST_RENDERER = "dynamicInfoListRenderer";
	private static final String COMPANY_ID = "companyId";
	
	@Activate
	@Modified
	public void activate(BundleContext bundleContext) {

		_registeredServices.forEach(serviceRegistration -> {
			
			serviceRegistration.unregister();
			
		});
		
		_registeredServices.clear();
		
		_companyLocalService.getCompanies().forEach(company -> {

			long companyId = company.getCompanyId();

			CustomInfoListRendererConfiguration configuration = getConfiguration(companyId);
			
			String[] tagNames = configuration.customTagNames();

			for(int i = 0; i < tagNames.length; i++) {

				_registeredServices.add(
						registerService(bundleContext, companyId, tagNames[i], CustomJournalArticleInfoListRenderer.class));
				_registeredServices.add(
						registerService(bundleContext, companyId, tagNames[i], CustomFileEntryInfoListRenderer.class));
				_registeredServices.add(
						registerService(bundleContext, companyId, tagNames[i], CustomObjectEntryInfoListRenderer.class));
				
			}			

		});
		
	}

	private ServiceRegistration registerService(BundleContext bundleContext, long companyId, String tagName, Class clazz) {
		
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(DYNAMIC_INFO_LIST_RENDERER, true);
        properties.put(COMPANY_ID, companyId);

        if(clazz.equals(CustomJournalArticleInfoListRenderer.class)) {

            return bundleContext.registerService(
            		InfoListRenderer.class, new CustomJournalArticleInfoListRenderer(companyId, tagName, _infoItemRendererRegistry), properties);

        } else if(clazz.equals(CustomFileEntryInfoListRenderer.class)) {

            return bundleContext.registerService(
            		InfoListRenderer.class, new CustomFileEntryInfoListRenderer(companyId, tagName, _infoItemRendererRegistry), properties);

        } else if(clazz.equals(CustomObjectEntryInfoListRenderer.class)) {

            return bundleContext.registerService(
            		InfoListRenderer.class, new CustomObjectEntryInfoListRenderer(companyId, tagName, _infoItemRendererRegistry), properties);

        } 
		
        return null;
        
	}
	
	private CustomInfoListRendererConfiguration getConfiguration(long companyId) {
		try {
			return _configurationProvider.getConfiguration(
					CustomInfoListRendererConfiguration.class,
					new CompanyServiceSettingsLocator(companyId, CustomInfoListRendererConfigurationConstants.CONFIGURATION_ID));
		}
		catch (ConfigurationException ce) {
			_log.error("Error initializing the configuration", ce);
		}

		return null;
	}
	
	@Reference
	private ConfigurationProvider _configurationProvider;
	
	@Reference
	private CompanyLocalService _companyLocalService;
	
	@Reference
	protected InfoItemRendererRegistry _infoItemRendererRegistry;
	
	private static final Log _log = LogFactoryUtil.getLog(CustomInfoListRendererFactory.class);

	private List<ServiceRegistration> _registeredServices = new ArrayList<>();
	
}
