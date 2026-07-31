# Tunisia Invest QA checklist

## Functional scenarios

- Submit the three-section hotel/Djerba sample and verify PayPal Sandbox order creation.
- Capture a completed PayPal order and verify the five gauges, three risks, and three recommendations.
- Disable or invalidate Groq temporarily and verify `generationSource: FALLBACK`, the warning banner, and saved data.
- Open History, view an existing analysis, relaunch it, and confirm all form fields are prefilled.
- Download `/api/analyse/{id}/pdf` and verify the PDF contains score, dimensions, risks, recommendations, and project data.
- Use the Business Plan button and verify the signed token redirect and prefilled receiving page.

## Error and performance scenarios

- Verify invalid form fields are rejected before payment.
- Verify Groq timeout is limited by `GROQ_TIMEOUT_SECONDS` (default 30 seconds).
- Verify PayPal invalid credentials return a clear `502` error without exposing secrets.
- Verify a missing analysis ID returns `404`.
- Check the dashboard at mobile widths and confirm gauges stack into one column.
