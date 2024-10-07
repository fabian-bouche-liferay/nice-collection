package com.liferay.samples.fbo.asset.info.custom;

import com.liferay.info.item.renderer.InfoItemRenderer;
import com.liferay.info.item.renderer.InfoItemRendererRegistry;
import com.liferay.info.item.renderer.InfoItemTemplatedRenderer;
import com.liferay.info.list.renderer.DefaultInfoListRendererContext;
import com.liferay.info.list.renderer.InfoListRendererContext;
import com.liferay.info.taglib.list.renderer.BasicInfoListRenderer;
import com.liferay.object.model.ObjectEntry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fabian-liferay
 */
public class CustomObjectEntryInfoListRenderer implements BasicInfoListRenderer<ObjectEntry>  {

	private String _tagName;
	private long _companyId;
	
	public CustomObjectEntryInfoListRenderer(long companyId, String tagName, InfoItemRendererRegistry infoItemRendererRegistry) {
		_companyId = companyId;
		_tagName = tagName;
		_infoItemRendererRegistry = infoItemRendererRegistry;
	}

	@Override
	public List<InfoItemRenderer<?>> getAvailableInfoItemRenderers() {
		return _infoItemRendererRegistry.getInfoItemRenderers(
			ObjectEntry.class.getName());
	}

	@Override
	public String getLabel(Locale locale) {
		return _tagName;
	}
	
	@Override
	public String getKey() {
		return this.getClass().getName() + "#" + _tagName;
	}
	
	@Override
	public void render(
		List<ObjectEntry> objectEntries, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		render(
			objectEntries,
			new DefaultInfoListRendererContext(
				httpServletRequest, httpServletResponse));
	}

	@Override
	public void render(
			List<ObjectEntry> objectEntries,
		InfoListRendererContext infoListRendererContext) {
		
		HttpServletRequest request = infoListRendererContext.getHttpServletRequest();
		HttpServletResponse response = infoListRendererContext.getHttpServletResponse();

		/*
		LayoutStructure layoutStructure = (LayoutStructure) request.getAttribute(LayoutWebKeys.LAYOUT_STRUCTURE);
		System.out.println("Layout Structure " + layoutStructure.getMainLayoutStructureItem().getItemConfigJSONObject().getString("name"));
		*/
		
		String listItemRendererKey =
			infoListRendererContext.getListItemRendererKey();

		if (Validator.isNull(listItemRendererKey)) {
			listItemRendererKey = "com.liferay.object.web.internal.info.item.renderer.ObjectEntryRowInfoItemRenderer";
		}

		String templateKey = infoListRendererContext.getTemplateKey();

		try {
			renderItems(objectEntries, request, response, listItemRendererKey, templateKey);
		} catch (IOException e) {
			_log.error("Failed to render", e);
		}
		
	}

	private void renderItems(List<ObjectEntry> objectEntries, HttpServletRequest request,
			HttpServletResponse response, String listItemRendererKey, String templateKey) throws IOException {
		InfoItemRenderer<ObjectEntry> infoItemRenderer = (InfoItemRenderer) _infoItemRendererRegistry.getInfoItemRenderer(listItemRendererKey);
		
		response.getWriter().append("<").append(_tagName).append(">");
		
		for (int i = 0; i < objectEntries.size(); i++) {

			response.getWriter().append("<div class=\"slot-item\" slot=\"item-").append(String.valueOf(i)).append("\" >");

			if(Validator.isNotNull(templateKey) && infoItemRenderer instanceof InfoItemTemplatedRenderer) {
				InfoItemTemplatedRenderer<ObjectEntry> infoItemTemplatedRenderer = (InfoItemTemplatedRenderer)infoItemRenderer;
				infoItemTemplatedRenderer.render(objectEntries.get(i), templateKey, request, response);
			} else {
				infoItemRenderer.render(objectEntries.get(i), request, response);
			}

			response.getWriter().append("</div>");
		}
		
		response.getWriter().append("</").append(_tagName).append(">");
	}

	@Override
	public String getListStyle() {
		return _tagName;
	}
	
	protected InfoItemRendererRegistry _infoItemRendererRegistry;

	private static final Log _log = LogFactoryUtil.getLog(
			CustomObjectEntryInfoListRenderer.class);
}