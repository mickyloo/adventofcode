use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::cmp::Ordering;
use std::collections::{HashMap, HashSet};
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "05";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn parse<R: BufRead>(reader: R) -> (HashMap<String, HashSet<String>>, Vec<Vec<String>>) {
        let blocks = read_to_blocks(reader);

        let mut rules_map: HashMap<String, HashSet<String>> = HashMap::new();
        blocks.get(0).unwrap().iter().for_each(|line| {
            let mut parts = line.split("|");
            let key = parts.next().unwrap().to_owned();
            rules_map
                .entry(key)
                .or_insert_with(HashSet::new)
                .insert(parts.next().unwrap().to_owned());
        });

        let pages: Vec<_> = blocks
            .get(1)
            .unwrap()
            .iter()
            .map(|line| {
                let p = line
                    .split(",")
                    .map(|x| x.to_owned())
                    .collect::<Vec<_>>();
                p
            })
            .collect();
        (rules_map, pages)
    }

    fn is_valid(rules: &HashMap<String, HashSet<String>>, report: &Vec<String>) -> bool {
        let res = report
            .iter()
            .tuple_windows()
            .map(|(left, right)| match rules.get(right) {
                Some(set) => !set.contains(left),
                None => true,
            })
            .collect::<Vec<_>>()
            .iter()
            .all(|x| *x == true);
        res
    }

    fn cmp(rules: HashMap<String, HashSet<String>>, left: &String, right: &String) -> Ordering {
        if rules.contains_key(left) {
            let set = rules.get(left).unwrap();
            if set.contains(right) {
                return Ordering::Greater;
            }
        }
        if rules.contains_key(right) {
            let set = rules.get(right).unwrap();
            if set.contains(left) {
                return Ordering::Less;
            }
        }
        Ordering::Equal
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let (rules, pages) = parse(reader);
        let (valids, _): (Vec<_>, Vec<_>) = pages.into_iter().partition(|x| is_valid(&rules, x));

        let mut answer = 0;
        valids.iter().for_each(|x| {
            let index = (x.len() - 1) / 2;
            answer += x[index].parse::<usize>().unwrap()
        });

        Ok(answer)
    }

    assert_eq!(143, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let (rules, pages) = parse(reader);
        let (_, mut invalids): (Vec<_>, Vec<_>) =
            pages.into_iter().partition(|x| is_valid(&rules, x));

        invalids.iter_mut().for_each(|invalid| {
            invalid.sort_by(|a, b| cmp(rules.clone(), a, b));
        });

        let mut answer = 0;
        invalids.iter().for_each(|x| {
            let index = (x.len() - 1) / 2;
            answer += x[index].parse::<usize>().unwrap()
        });

        Ok(answer)
    }

    assert_eq!(123, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
