<html>
	<head>
		<style>
			body{font-family: arial, sans-serif; letter-spacing: .01em; background-color: #f7f7f7; font-size: 11pt; line-height: 1.5em;margin: 0;}
			h1{clear: both; overflow: hidden; }
			h1 .logo{float: right;}
			.indent{padding-left: 24px;}
			.content{margin: 48px  auto; width: 80%; }
			pre{display: block; padding: 1em; background-color: white; border: solid 1px #ebebeb; }
			table{display: block;  margin-top: 1em;border-collapse: collapse;font-size: 11pt; }
			td, th{ border: solid 1px #ebebeb; padding: .7em;}
			td{background-color: white; }
			th{background-color: #ebebeb; text-align: left; }
			
			.url-dynamic-parameter-tenant{color:  #60a917; font-weight: bold;}
			.url-dynamic-parameter-smartobject{color:  #0050ef;font-weight: bold;}
			.url-dynamic-parameter-stream{color:  #d80073;font-weight: bold;}
			.url-dynamic-parameter-dataset{color:  #ba00ff;font-weight: bold;}
			.url-dynamic-parameter-package-id{color:  #ff9900;font-weight: bold;}
			
			footer{background-color: black; color: white; padding: 24px; text-align: center;}
			footer  a{color: white; text-decoration: none; padding: 0 48px;}

		</style>
	</head>
	<body>
		<div class='content'>
			<h1>
				Smartdatanet Metadata API
				<div class='logo'><img src='https://userportal.smartdatanet.it/userportal/img/familyidentity/logosdp-top-navbar.png' alt='Smartdatanet'/></div>
			</h1>
			<p>Esposizione API di ricerca e dettaglio dei Metadati di stream e dataset Contenuti nella Smartdata Platform<p>
			<h2>Ricerca</h2>
			<div class='indent'>
				<p>
					<strong>URL Base ricerca in formato JSON</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/search/full?</code></pre>
				</p>
				<p>
					<strong>URL Base ricerca in formato CKAN</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list?</code></pre>
				</p>
				<strong>Parametri</strong> 
				<table>
					<thead><tr><th>Key</th><th>Desc</th><th>Valori previsti</th></tr></thead>
					<tbody>
						<tr><td>q</td><td>query di ricerca</td><td>Testo libero</td></tr>
						<tr><td>tenant</td><td>Codice del tenant propietario dello stream/dataset</td><td>Se indicato un codice tenant non censito restituisce zero valori </td></tr>
						<tr><td>domain</td><td>Codice dell'ambito dello stream/dataset</td><td>Se indicato un codice ambito non censito restituisce zero valori </td></tr>
						<tr><td>opendata</td><td>Propriet&agrave; del dato/stream di essere opendata</td><td>Se indicato opendata=true filtra estraendo i dati opendata, se indicato con valore diverso da true estrae solo i dati NON opendata. Se omesso non filtra sulla propriet&agrave; opendata</td></tr>
						<tr><td>start</td><td>Paginazione: prima riga da estrarre</td><td>Campo numerico intero (se omesso si parte da zero)</td></tr>
						<tr><td>end</td><td>Paginazione: numero di righe da estrarre</td><td>Campo numerico intero (se omesso vengono estratte 12 righe). Valore massimo: 1000</td></tr>
						<tr><td>lang</td><td>Lingua in cui si desidera avere l'ambito del e i tag associati al dataset/stream</td><td>Gestiti italiano (lang=it) e inglese (lang=en)</td></tr>
					</tbody>
				</table>
			</div>
			<h2>Dettaglio</h2>
			<div class='indent'>
				<p>
					<strong>URL Base dettaglio stream in formato JSON</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span>/<span class='url-dynamic-parameter-stream'>{{streamCode}}</span>?</code></pre>
				dove: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> &egrave; il codice dell'organizzazione proprietaria dello stream </li>
						<li><span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span> &egrave; il codice dello Smart Object utilizzato dallo stream</li>
						<li><span class='url-dynamic-parameter-stream'>{{streamCode}}</span> &egrave; il codice dello stream </li>
					</ul>
				</p>
				<p>

					<strong>URL Base dettaglio dataset in formato JSON</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span></code></pre>
					dove: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> &egrave; il codice dell'organizzazione proprietaria dello stream</li>
						<li><span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span> &egrave; il codice del dataset </li>
					</ul>
				</p>
				<p>
					<strong>URL Base ricerca in formato CKAN</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list/<span class='url-dynamic-parameter-package-id'>{{packageId}}</span></code></pre> 
					dove: 
					<ul>
						<li><span class='url-dynamic-parameter-package-id'>{{packageId}}</span> &egrave; il packageId preso dalla lista restituita dalla ricerca (sempre in formato ckan)</li>
					</ul>
				</p>
			
				<strong>Parametri</strong> 
				<table>
					<thead><tr><th>Key</th><th>Desc</th><th>Valori previsti</th></tr></thead>
					<tbody>
						<tr><td>lang</td><td>Lingua in cui si desidera avere l'ambito del e i tag associati al dataset/stream</td><td>Gestiti italiano (lang=it) e inglese (lang=en)</td></tr>
					</tbody>
				</table>
			</div>
		</div>
		<footer>
			<div>
				<a class="logo-sdp" href="http://www.smartdatanet.it" target='_blank'>Smartdatenet.it</a> | 
				<a class="logo-rp" href="http://www.regione.piemonte.it" target='_blank'>Regione Piemonte</a> | 
				<a class="logo-csi" href="http://www.csipiemonte.it" target='_blank'><span class="hidden">CSI Piemonte</span></a> 
			</div>
		</footer>
	</body>
</html>

