export const inNumeric = value => {
  return !isNaN(parseFloat(value)) && isFinite(value);
}
