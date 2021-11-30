<template>
  <header-nav/>
  <ol-map/>
</template>

<script>

import HeaderNav from "@/components/HeaderNav";
import OlMap from "@/components/OlMap";

export default {
  name: 'Home',
  components: {OlMap, HeaderNav},
}
</script>
