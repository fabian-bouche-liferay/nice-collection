package com.liferay.samples.fbo.asset.info.custom;

import com.liferay.info.item.renderer.InfoItemRenderer;
import com.liferay.info.item.renderer.InfoItemRendererRegistry;
import com.liferay.info.item.renderer.InfoItemTemplatedRenderer;
import com.liferay.info.list.renderer.DefaultInfoListRendererContext;
import com.liferay.info.list.renderer.InfoListRendererContext;
import com.liferay.info.taglib.list.renderer.BasicInfoListRenderer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fabian-liferay
 */
public class CustomFileEntryInfoListRenderer implements BasicInfoListRenderer<FileEntry>  {

	private String _tagName;
	private long _companyId;
	
	public CustomFileEntryInfoListRenderer(long companyId, String tagName, InfoItemRendererRegistry infoItemRendererRegistry) {
		_companyId = companyId;
		_tagName = tagName;
		_infoItemRendererRegistry = infoItemRendererRegistry;
	}

	@Override
	public List<InfoItemRenderer<?>> getAvailableInfoItemRenderers() {
		return _infoItemRendererRegistry.getInfoItemRenderers(
			FileEntry.class.getName());
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
		List<FileEntry> fileEntries, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		render(
				fileEntries,
			new DefaultInfoListRendererContext(
				httpServletRequest, httpServletResponse));
	}

	@Override
	public void render(
			List<FileEntry> fileEntries,
		InfoListRendererContext infoListRendererContext) {
		
		HttpServletRequest request = infoListRendererContext.getHttpServletRequest();
		HttpServletResponse response = infoListRendererContext.getHttpServletResponse();
		
		String listItemRendererKey =
			infoListRendererContext.getListItemRendererKey();

		if (Validator.isNull(listItemRendererKey)) {
			listItemRendererKey = "com.liferay.document.library.web.internal.info.item.renderer.FileEntryTitleInfoItemRenderer";
		}

		String templateKey = infoListRendererContext.getTemplateKey();

		try {
			renderItems(fileEntries, request, response, listItemRendererKey, templateKey);
		} catch (IOException e) {
			_log.error("Failed to render", e);
		}
		
	}

	private void renderItems(List<FileEntry> fileEntries, HttpServletRequest request,
			HttpServletResponse response, String listItemRendererKey, String templateKey) throws IOException {
		InfoItemRenderer<FileEntry> infoItemRenderer = (InfoItemRenderer) _infoItemRendererRegistry.getInfoItemRenderer(listItemRendererKey);
		
		response.getWriter().append("<").append(_tagName).append(">");
		
		for (int i = 0; i < fileEntries.size(); i++) {

			response.getWriter().append("<div class=\"slot-item\" slot=\"item-").append(String.valueOf(i)).append("\" >");

			if(Validator.isNotNull(templateKey) && infoItemRenderer instanceof InfoItemTemplatedRenderer) {
				InfoItemTemplatedRenderer<FileEntry> infoItemTemplatedRenderer = (InfoItemTemplatedRenderer)infoItemRenderer;
				infoItemTemplatedRenderer.render(fileEntries.get(i), templateKey, request, response);
			} else {
				infoItemRenderer.render(fileEntries.get(i), request, response);
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
			CustomFileEntryInfoListRenderer.class);
}